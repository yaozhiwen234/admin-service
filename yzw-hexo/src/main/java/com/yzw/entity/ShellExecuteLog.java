package com.yzw.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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
public class    ShellExecuteLog extends Model<ShellExecuteLog> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String shellHost;

    private Long shellBaseId;

    /**
     * 0 未执行 1成功 2 失败
     */
    private Integer status;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getShellHost() {
        return shellHost;
    }

    public void setShellHost(String shellHost) {
        this.shellHost = shellHost;
    }
    public Long getShellBaseId() {
        return shellBaseId;
    }

    public void setShellBaseId(Long shellBaseId) {
        this.shellBaseId = shellBaseId;
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ShellExecuteLog{" +
            "id=" + id +
            ", shellHost=" + shellHost +
            ", shellBaseId=" + shellBaseId +
            ", status=" + status +
            ", updateTime=" + updateTime +
            ", createTime=" + createTime +
        "}";
    }
}
