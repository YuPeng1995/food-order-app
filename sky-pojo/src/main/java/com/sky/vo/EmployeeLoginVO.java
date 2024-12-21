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
@ApiModel(description = "Data format for employee login")
public class EmployeeLoginVO implements Serializable {

    @ApiModelProperty("Employee id")
    private Long id;

    @ApiModelProperty("Employee username")
    private String userName;

    @ApiModelProperty("Employee name")
    private String name;

    @ApiModelProperty("JWT token")
    private String token;

}
