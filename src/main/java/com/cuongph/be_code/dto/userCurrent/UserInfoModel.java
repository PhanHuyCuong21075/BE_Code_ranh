package com.cuongph.be_code.dto.userCurrent;

import com.cuongph.be_code.common.enums.ETokenType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class UserInfoModel implements Serializable {
    private static final long serialVersionUID = 3275591372097566411L;
    private Long id;
    private String type;
    private String userName;
    private UserDetailModel userDetail;
    private String email;
    private Date expireAt;
    private List<String> scopes;
    private List<String> authorities;
    //    private List<MenuModel> menus;
    private ETokenType eTokenType;
    private Long twoFactor;


}
