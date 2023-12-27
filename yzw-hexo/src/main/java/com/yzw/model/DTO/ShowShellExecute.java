package com.yzw.model.DTO;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author yaozw
 * @Date 2023/12/25 22:02
 * @Description:
 */

@Data
public class ShowShellExecute {

    private String shellDescribe;

    private String shellUserName;

    private String shellUserPassword;

    private String shellCommand;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;

    private String shellHost;

    private Integer status;

    private String statusVal;
    private String shellBaseId;
}
