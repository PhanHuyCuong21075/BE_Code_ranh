package com.cuongph.be_code.common.config.provider;

import com.cuongph.be_code.repo.UserDetailRepository;
import com.cuongph.be_code.service.UsersService;
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


    private Date expireAt(ETokenType ETokenType) {
        if (ETokenType.ACCESS_TOKEN.equals(ETokenType)) {
            return new Date(new Date().getTime() + appProperties.jwtAccessValidity);
        } else if (ETokenType.REFRESH_TOKEN.equals(ETokenType)) {
            return new Date(new Date().getTime() + appProperties.jwtRefreshValidity);
        }
        return new Date(new Date().getTime() + appProperties.jwtCheckValidity);
    }

    private boolean authenticated(UsersEntity users, String password) {
        if (!appProperties.devMode) {
            boolean checkPass = encoder.matches(password, users.getUserPass());

            return checkPass;
        } else {
            return true;
        }
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var userName = authentication.getName();
        var password = authentication.getCredentials().toString();

        UsersEntity users = usersService.getByUserName(userName.toLowerCase().trim());

        if (users != null && Objects.equals(EActive.ACTIVE.value, users.getIsActive())) {
            if (authenticated(users, password)) {
                List<String> roleCode = rolesRepo.getRoleCodeByUserName(users.getUserName());
                var authorizes = roleCode.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
                List<String> scopes = new ArrayList<String>();
                UserInfoModel userInfoDTO = new UserInfoModel();
                UserDetailEntity userDetail = userDetailRepo.findByUserName(userName);

                userInfoDTO.setId(users.getId());
                userInfoDTO.setType(users.getRefUserType());
                userInfoDTO.setUserName(users.getUserName());
                userInfoDTO.setExpireAt(expireAt(ETokenType.ACCESS_TOKEN));
                userInfoDTO.setETokenType(ETokenType.ACCESS_TOKEN);
                userInfoDTO.setScopes(scopes);

                if (userDetail != null) userInfoDTO.setEmail(userDetail.getEmail());
                userInfoDTO.setAuthorities(roleCode);
                userInfoDTO.setTwoFactor(users.getTwoFactorAuthentication());
                userInfoDTO.setUserDetail(ModelMapperUtils.toObject(userDetail, UserDetailModel.class));

                Gson gson = new Gson();
                return new UsernamePasswordAuthenticationToken(gson.toJson(userInfoDTO), password, authorizes);
            } else {
                throw new CustomAuthenticationException("PASSWORD_OR_ACCOUNT_IN_ACTIVE");
            }
        } else {
            throw new CustomAuthenticationException("USER_NOT_FOUND");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
