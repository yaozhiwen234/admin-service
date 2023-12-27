package com.yzw.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.yzw.entity.ShellBase;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yzw.model.DTO.ShowShellExecute;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yzw
 * @since 2023-12-24
 */
public interface ShellBaseMapper extends BaseMapper<ShellBase> {


    @Select("SELECT a.shell_base_id, a.status,a.shell_host,b.* FROM `shell_execute_log` a LEFT JOIN shell_base b on a.shell_base_id = b.id ${ew.customSqlSegment}")
    IPage<ShowShellExecute> selectPages(IPage page, @Param(Constants.WRAPPER) Wrapper<ShellBase> queryWrapper);
}
