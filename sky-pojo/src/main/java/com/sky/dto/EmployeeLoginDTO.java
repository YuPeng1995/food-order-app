package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "Employee login data format")
public class EmployeeLoginDTO implements Serializable {

    @ApiModelProperty("Employee username")
    private String username;

    @ApiModelProperty("Employee password")
    private String password;

}
