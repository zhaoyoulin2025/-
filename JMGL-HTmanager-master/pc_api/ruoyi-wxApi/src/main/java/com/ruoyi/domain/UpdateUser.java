package com.ruoyi.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdateUser {

    private Long userId;

    private Long clientId;

    @NotBlank(message = "用户昵称不能为空")
    private String nickName;

    @NotBlank(message = "用户手机号不能为空")
    private String phonenumber;

    @NotBlank(message = "用户头像不能为空")
    private String avatar;

}
