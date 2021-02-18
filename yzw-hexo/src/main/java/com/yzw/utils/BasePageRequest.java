package com.yzw.utils;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author: yzw
 * @create: 2020/5/12 0012
 * @description:
 **/

@Getter
@Setter
public class BasePageRequest implements Serializable {


    @ApiModelProperty(value = "分页页码，第一页  是1")
    private Integer pageNumber;

    @ApiModelProperty(value = "分页记录数")
    private Integer pageSize;

    public BasePageRequest() {
        this.pageNumber = 0;
        this.pageSize = 0;
    }

    public BasePageRequest(Integer pageNumber, Integer pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

}
