package com.sky.controller.user;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.UserEmailConfirmationDTO;
import com.sky.dto.UserLoginDTO;
import com.sky.dto.UserRegisterDTO;
import com.sky.entity.User;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.UserService;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("user/user")
@Api(tags = "User Apis")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("/login")
    @ApiOperation("User login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("User login: {}", userLoginDTO);
        User user = userService.login(userLoginDTO);

        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);

        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .userName(user.getUsername())
                .email(user.getEmail())
                .token(token)
                .build();

        return Result.success(userLoginVO);
    }

    @PostMapping("/register")
    @ApiOperation("User register")
    public Result register(@RequestBody UserRegisterDTO userRegisterDTO) {
        log.info("User register: {}", userRegisterDTO);
        userService.register(userRegisterDTO);
        return Result.success();
    }

    @PostMapping("/confirm_email")
    @ApiOperation("Confirm email")
    public Result confirmEmail(@RequestBody UserEmailConfirmationDTO userEmailConfirmationDTO) {
        log.info("Confirm email: {}", userEmailConfirmationDTO);
        userService.confirmEmail(userEmailConfirmationDTO);
        return Result.success();
    }

}

