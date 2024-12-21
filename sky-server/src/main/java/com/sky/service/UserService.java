package com.sky.service;

import com.sky.dto.UserEmailConfirmationDTO;
import com.sky.dto.UserLoginDTO;
import com.sky.dto.UserRegisterDTO;
import com.sky.entity.User;

public interface UserService {

    User login(UserLoginDTO userLoginDTO);

    void register(UserRegisterDTO userRegisterDTO);

    void confirmEmail(UserEmailConfirmationDTO userEmailConfirmationDTO);

}
