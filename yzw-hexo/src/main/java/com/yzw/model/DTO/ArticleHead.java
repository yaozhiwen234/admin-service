package com.yzw.model.DTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author yzw
 * @date 2021/1/15
 */
@Data
@ApiModel(value = "接收头部文章的参数" , description = "接收请求参数类")
public class ArticleHead {

    @NotNull
    @NotEmpty
    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("标签")
    private String tags;

    @ApiModelProperty("分类")
    private String categories;

    @ApiModelProperty("图片连接")
    private String cover;
}
