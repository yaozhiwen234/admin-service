package com.yzw.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author: liwei
 * @create: 2020/5/13 0013
 * @description:
 **/
@Getter
@Setter
@ApiModel("分页数据返回体")
public class BasePageResponse<T> {

    // 当前页码
    @ApiModelProperty(value = "当前页码")
    private Long current;

    // 当前页数量
    @ApiModelProperty(value = "当前页数量")
    private Long size;

    // 总页数
    @ApiModelProperty(value = "总页数")
    private Long pages;

    // 总数量
    @ApiModelProperty(value = "总数量")
    private Long total;

    @ApiModelProperty(value = "数据")
    private List<T> record;


}
