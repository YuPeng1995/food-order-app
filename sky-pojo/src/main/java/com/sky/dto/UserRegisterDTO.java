package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "User registration data format")
public class UserRegisterDTO implements Serializable {

    @ApiModelProperty("User username")
    private String username;

    @ApiModelProperty("User email")
    private String email;

    @ApiModelProperty("User password")
    private String password;

    @ApiModelProperty("User phone")
    private String phone;

}
