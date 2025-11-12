package com.cuongph.be_code.dto.userCurrent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserDetailModel implements Serializable {
    private static final long serialVersionUID = -6958780377365570957L;

    private String userName;
    private String fullName;
    private String phone;
    private String email;
    private String gender;
    private String filePathAvatar;
}
