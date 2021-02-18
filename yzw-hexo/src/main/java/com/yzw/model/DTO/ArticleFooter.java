package com.yzw.model.DTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yzw
 * @date 2021/1/15
 */
@ApiModel(value = "添加文章", description =  "接收请求参数")
@Data
public class ArticleFooter {
    @ApiModelProperty("主键id")
    private Integer id;

    @ApiModelProperty("操作动作")
    private  String operation;

    @ApiModelProperty("文章标题")
    private String title;

    @ApiModelProperty("文章")
    private String text;
}
