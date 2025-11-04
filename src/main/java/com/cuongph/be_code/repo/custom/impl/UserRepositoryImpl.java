package com.cuongph.be_code.repo.custom.impl;

import com.cuongph.be_code.repo.custom.UserRepositoryCustom;
import com.cuongph.be_code.repo.custom.query.UserRepositoryQuery;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl extends UserRepositoryQuery implements UserRepositoryCustom {


}
