package com.yzw.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author yzw
 * @since 2021-01-14
 */
@Data
public class Article extends Model<Article> {

    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 日期
     */
    private LocalDateTime date;

    /**
     * 标签
     */
    private String tags;

    /**
     * 分类
     */
    private String categories;

    /**
     * 图片连接
     */
    private String cover;

    /**
     * 文本
     */
    private String text;

    /**
     * 数据是否同步
     */
    private  Integer state;

    /**
     * 创建时间
     */
    @TableField("createTime")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField("updateTime")
    private LocalDateTime updateTime;


}
