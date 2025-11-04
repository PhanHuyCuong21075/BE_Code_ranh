package com.cuongph.be_code.repo.custom;

import java.util.List;

public interface RolesRepositoryCustom {
    List<String> getRoleCodeByUserName(String userName);

}
