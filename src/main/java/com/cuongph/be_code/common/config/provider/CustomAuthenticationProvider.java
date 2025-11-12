package com.cuongph.be_code.common.config.provider;

import com.cuongph.be_code.common.AppProperties;
import com.cuongph.be_code.common.CustomAuthenticationException;
import com.cuongph.be_code.common.enums.EActiveStatus;
import com.cuongph.be_code.common.enums.ETokenType;
import com.cuongph.be_code.dto.userCurrent.UserDetailModel;
import com.cuongph.be_code.dto.userCurrent.UserInfoModel;
import com.cuongph.be_code.entity.UserEntity;
import com.cuongph.be_code.entity.UserDetailEntity;
import com.cuongph.be_code.repo.RolesRepository;
import com.cuongph.be_code.repo.UserDetailRepository;
import com.cuongph.be_code.service.UsersService;
import com.cuongph.be_code.utils.mapper.ModelMapperUtils;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UsersService usersService;

    @Autowired
    private UserDetailRepository userDetailRepo;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private RolesRepository rolesRepo;


    public CustomAuthenticationProvider(
            UsersService usersService,
            UserDetailRepository userDetailRepo,
            BCryptPasswordEncoder encoder,
            AppProperties appProperties,
            RolesRepository rolesRepo) {
        this.usersService = usersService;
        this.userDetailRepo = userDetailRepo;
        this.encoder = encoder;
        this.appProperties = appProperties;
        this.rolesRepo = rolesRepo;
    }

    private Date expireAt(ETokenType ETokenType) {
        if (ETokenType.ACCESS_TOKEN.equals(ETokenType)) {
            return new Date(new Date().getTime() + appProperties.jwtAccessValidity);
        } else if (ETokenType.REFRESH_TOKEN.equals(ETokenType)) {
            return new Date(new Date().getTime() + appProperties.jwtRefreshValidity);
        }
        return new Date(new Date().getTime() + appProperties.jwtCheckValidity);
    }

    private boolean authenticated(UserEntity users, String password) {
        if (!appProperties.devMode) {
            boolean checkPass = encoder.matches(password, users.getPassword());

            return checkPass;
        } else {
            return true;
        }
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName().toLowerCase().trim();
        String password = authentication.getCredentials().toString();

        Optional<UserEntity> userOpt = usersService.getByUserName(username);

        // 1. Kiểm tra user có tồn tại không
        if (userOpt.isEmpty()) {
            throw new CustomAuthenticationException("USER_NOT_FOUND");
        }

        UserEntity userEntity = userOpt.get();

        // 2. Kiểm tra trạng thái hoạt động
        if (!Objects.equals(EActiveStatus.ACTIVE.value, userEntity.getIsActive())) {
            throw new CustomAuthenticationException("ACCOUNT_INACTIVE");
        }

        // 3. Kiểm tra mật khẩu
        if (!authenticated(userEntity, password)) {
            throw new CustomAuthenticationException("INVALID_PASSWORD");
        }

        // 4. Lấy role
        List<String> roleCodes = rolesRepo.getRoleCodeByUserId(userEntity.getId());
        var authorities = roleCodes.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // 5. Lấy user detail bằng userId
        UserDetailEntity userDetailEntity = userDetailRepo.findByUserId(userEntity.getId());

        // 6. Tạo đối tượng Principal (UserInfoModel) để trả về
        UserInfoModel userInfo = new UserInfoModel();

        // Lấy thông tin từ UserEntity
        userInfo.setId(userEntity.getId());
        userInfo.setUserName(userEntity.getUsername());
        // userInfo.setEmail(userEntity.getEmail());

        // Map UserDetailEntity sang UserDetailModel
        if (userDetailEntity != null) {
            UserDetailModel userDetailModel = ModelMapperUtils.toObject(userDetailEntity, UserDetailModel.class);
            userInfo.setUserDetail(userDetailModel);
        }

        // Lấy thông tin Roles (đã query ở Bước 4)
        userInfo.setAuthorities(roleCodes);

        // Thiết lập thông tin token (từ logic trong class của bạn)
        userInfo.setETokenType(ETokenType.ACCESS_TOKEN);
        // Gọi hàm private 'expireAt' mà bạn đã định nghĩa
        userInfo.setExpireAt(expireAt(ETokenType.ACCESS_TOKEN));

        // 7. Trả kết quả xác thực
        String principalJson = new Gson().toJson(userInfo);

        // 'authorities' ở đây là List<SimpleGrantedAuthority> đã tạo ở Bước 4
        return new UsernamePasswordAuthenticationToken(principalJson, password, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
