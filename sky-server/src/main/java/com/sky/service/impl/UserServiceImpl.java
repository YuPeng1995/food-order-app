package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.dto.UserEmailConfirmationDTO;
import com.sky.dto.UserLoginDTO;
import com.sky.dto.UserRegisterDTO;
import com.sky.entity.User;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.UserMapper;
import com.sky.service.UserService;
import com.sky.utils.EmailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public User login(UserLoginDTO userLoginDTO) {
        String email = userLoginDTO.getEmail();
        String password = userLoginDTO.getPassword();

        User user = userMapper.getByEmail(email);

        if (user == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(user.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        Integer status = user.getStatus();
        if (status == 0) {
            //账号未激活
            throw new RuntimeException(MessageConstant.EMAIL_NOT_CONFIRMED);
        }

        //3、返回实体对象
        return user;

    }

    @Override
    public void register(UserRegisterDTO userRegisterDTO) {

        String verificationCode = EmailUtil.generateCode();

        redisTemplate.opsForValue().set(
                "verification:" + userRegisterDTO.getEmail(),
                verificationCode,
                5,
                TimeUnit.MINUTES
        );

        SimpleMailMessage message = EmailUtil.prepareVerificationEmail(userRegisterDTO.getEmail(), verificationCode);

        emailSender.send(message);

        User user = new User();
        BeanUtils.copyProperties(userRegisterDTO, user);
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setStatus(0);

        userMapper.insert(user);

    }

    @Override
    public void confirmEmail(UserEmailConfirmationDTO userEmailConfirmationDTO) {
        String email = userEmailConfirmationDTO.getEmail();
        String code = userEmailConfirmationDTO.getCode();System.out.println(code);

        String key = "verification:" + email;
        String verificationCode = (String) redisTemplate.opsForValue().get(key);

        if (verificationCode == null || !verificationCode.equals(code)) {
            //验证码错误
            throw new RuntimeException(MessageConstant.VERIFICATION_CODE_ERROR);
        }

        User user = userMapper.getByEmail(email);
        user.setStatus(1);
        user.setUpdateTime(LocalDateTime.now());
        userMapper.update(user);
    }

}
