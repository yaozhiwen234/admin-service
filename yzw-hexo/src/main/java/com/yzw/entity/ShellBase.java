package com.yzw.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author yzw
 * @since 2023-12-24
 */

@Data
public class ShellBase extends Model<ShellBase> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String shellDescribe;

    private String shellUserName;

    private String shellUserPassword;

    private String shellCommand;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;


}
