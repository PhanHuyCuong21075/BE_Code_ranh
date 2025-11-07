package com.cuongph.be_code.common.component;

import com.cuongph.be_code.dto.userCurrent.UserInfoModel;
import com.google.gson.Gson;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UsersContext {

    public UserInfoModel getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Gson gson = new Gson();
        return gson.fromJson(authentication.getPrincipal().toString(), UserInfoModel.class);
    }

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 1. Lấy principal, nó đã là UserInfoModel
        Object principal = authentication.getPrincipal();

        // 2. Ép kiểu (cast) nó sang UserInfoModel
        UserInfoModel currentUser = (UserInfoModel) principal;

        // 3. Trả về ID
        return currentUser.getId();
    }

    public String getCurrentUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Gson gson = new Gson();
        var currentUser = gson.fromJson(authentication.getPrincipal().toString(), UserInfoModel.class);

        return currentUser.getUserName();
    }
}