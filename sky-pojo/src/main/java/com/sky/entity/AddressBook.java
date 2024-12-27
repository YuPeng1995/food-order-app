package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 地址簿
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressBook implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    //用户id
    private Long userId;

    //收货人
    private String name;

    //手机号
    private String phoneNumber;

    private String cityCode;

    private String stateName;

    private String cityName;

    private String zipCode;

    private String streetAddress;

    //标签
    private String label;

    //是否默认 0否 1是
    private Integer isDefault;
}
