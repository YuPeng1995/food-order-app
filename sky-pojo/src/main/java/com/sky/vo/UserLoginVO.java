package com.sky.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Data format for user login")
public class UserLoginVO implements Serializable {

    @ApiModelProperty("User id")
    private Long id;

    @ApiModelProperty("Username")
    private String userName;

    @ApiModelProperty("Email")
    private String email;

    @ApiModelProperty("JWT token")
    private String token;

}
