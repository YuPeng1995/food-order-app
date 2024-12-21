package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * C端用户登录
 */
@Data
@ApiModel(description = "User login data format")
public class UserLoginDTO implements Serializable {

    @ApiModelProperty("User email")
    private String email;

    @ApiModelProperty("User password")
    private String password;

}
