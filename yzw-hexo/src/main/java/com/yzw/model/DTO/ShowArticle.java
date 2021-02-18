package com.yzw.model.DTO;

import com.alibaba.fastjson.annotation.JSONField;
import com.yzw.utils.BasePageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * @author yzw
 * @date 2021/1/15
 */

@Data
@ApiModel(value = "查找文章" , description = "根据以下条件查找文章")
public class ShowArticle extends BasePageRequest {

    @ApiModelProperty("标题")
    private String title;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(name = "startTime")
    @ApiModelProperty("起始日期")
    private LocalDate startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(name = "endTime")
    @ApiModelProperty("结束日期")
    private LocalDate endTime;

    @ApiModelProperty("标签")
    private String tags;

    @ApiModelProperty("分类")
    private String categories;
}
