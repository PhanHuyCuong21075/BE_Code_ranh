package com.cuongph.be_code.dto.response;

import com.cuongph.be_code.entity.UserEntity;
import lombok.Data;

@Data
public class PendingFriendResponse {

    private UserEntity user;
    private String type;
}
