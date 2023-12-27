package com.yzw.model.DTO;

import com.alibaba.fastjson.annotation.JSONField;
import com.yzw.utils.BasePageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * @Author yaozw
 * @Date 2023/12/25 21:46
 * @Description:
 */


@Data
@ApiModel(value = "shell执行列表", description = "接收请求参数类")
public class ExecuteCommandVo extends BasePageRequest {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(name = "startTime")
    @ApiModelProperty("起始日期")
    private LocalDate startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(name = "endTime")
    @ApiModelProperty("结束日期")
    private LocalDate endTime;

    private String ip;

    private String shellDescribe;

}
