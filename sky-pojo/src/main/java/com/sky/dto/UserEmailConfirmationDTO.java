package com.sky.dto;

import lombok.Data;

@Data
public class UserEmailConfirmationDTO {

    private String email;

    private String code;

}
