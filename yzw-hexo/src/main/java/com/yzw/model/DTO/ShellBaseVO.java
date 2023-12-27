package com.yzw.model.DTO;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author yaozw
 * @Date 2023/12/24 14:09
 * @Description:
 */

@Data
@ApiModel(value = "机器分组", description = "接收请求参数类")
public class ShellBaseVO {

    private Long Id;
    @NotBlank
    private String shellDescribe;

    @NotBlank
    private String shellUserName;

    @NotBlank
    private String shellUserPassword;

    @NotBlank
    private String shellCommand;
}
