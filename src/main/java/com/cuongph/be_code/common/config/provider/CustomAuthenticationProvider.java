package com.cuongph.be_code.common.config.provider;

import com.cuongph.be_code.common.AppProperties;
import com.cuongph.be_code.common.CustomAuthenticationException;
import com.cuongph.be_code.common.enums.EActiveStatus;
import com.cuongph.be_code.common.enums.ETokenType;
import com.cuongph.be_code.dto.userCurrent.UserDetailModel;
import com.cuongph.be_code.dto.userCurrent.UserInfoModel;
import com.cuongph.be_code.entity.User;
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

import java.util.ArrayList;
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

    private boolean authenticated(User users, String password) {
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

        Optional<User> userOpt = usersService.getByUserName(username);

        // 1. Kiểm tra user có tồn tại không
        if (userOpt.isEmpty()) {
            throw new CustomAuthenticationException("USER_NOT_FOUND");
        }

        User user = userOpt.get();

        // 2. Kiểm tra trạng thái hoạt động
        if (!Objects.equals(EActiveStatus.ACTIVE.value, user.getIsActive())) {
            throw new CustomAuthenticationException("ACCOUNT_INACTIVE");
        }

        // 3. Kiểm tra mật khẩu
        if (!authenticated(user, password)) {
            throw new CustomAuthenticationException("INVALID_PASSWORD");
        }

        // 4. Lấy quyền (role)
        List<String> roleCodes = rolesRepo.getRoleCodeByUserName(user.getUsername());
        var authorities = roleCodes.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // 5. Chuẩn bị thông tin người dùng trả về
        List<String> scopes = new ArrayList<>();
        UserDetailEntity userDetailEntity = userDetailRepo.findByUserName(username);

        UserInfoModel userInfoDTO = new UserInfoModel();
        userInfoDTO.setId(user.getId());
        userInfoDTO.setUserName(user.getUsername());
        userInfoDTO.setExpireAt(expireAt(ETokenType.ACCESS_TOKEN));
        userInfoDTO.setETokenType(ETokenType.ACCESS_TOKEN);
        userInfoDTO.setScopes(scopes);
        userInfoDTO.setAuthorities(roleCodes);

        if (userDetailEntity != null) {
            userInfoDTO.setEmail(userDetailEntity.getEmail());
            userInfoDTO.setUserDetail(ModelMapperUtils.toObject(userDetailEntity, UserDetailModel.class));
        }

        // 6. Trả kết quả xác thực
        String principalJson = new Gson().toJson(userInfoDTO);
        return new UsernamePasswordAuthenticationToken(principalJson, password, authorities);
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
