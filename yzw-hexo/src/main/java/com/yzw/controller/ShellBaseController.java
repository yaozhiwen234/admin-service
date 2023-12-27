package com.yzw.controller;


import cn.hutool.core.thread.ThreadUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.yzw.entity.ShellBase;
import com.yzw.entity.ShellExecuteLog;
import com.yzw.mapper.ShellBaseMapper;
import com.yzw.model.DTO.ExecuteCommandVo;
import com.yzw.model.DTO.ShellBaseVO;
import com.yzw.model.DTO.ShowShellExecute;
import com.yzw.service.IShellBaseService;
import com.yzw.service.IShellExecuteLogService;
import com.yzw.utils.BasePageResponse;
import com.yzw.utils.JsonResult;
import com.yzw.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.parser.Token;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author yzw
 * @since 2023-12-24
 */
@RequestMapping("/api/shellBase")
@RestController
@Api(tags = "shellBase管理")
@RequiredArgsConstructor
@PropertySource("classpath:config/application.yml")//读取application.yml文件
@Slf4j
public class ShellBaseController {


    @Autowired
    private IShellBaseService shellBaseService;

    @Autowired
    private IShellExecuteLogService shellExecuteLogService;

    @Autowired
    private ShellBaseMapper shellBaseMapper;

    @ApiOperation("添加linux执行分组")
    @PostMapping("/addShellBase")
    public JsonResult addShellBase(@RequestBody @Validated ShellBaseVO vo) {
        ModelMapper mapper = new ModelMapper();
        ShellBase shellBase = mapper.map(vo, ShellBase.class);
        shellBase.setCreateTime(LocalDateTime.now());
        shellBase.setUpdateTime(LocalDateTime.now());
        shellBaseService.save(shellBase);
        return JsonResult.success();
    }

    @ApiOperation("查询linux执行分组")
    @GetMapping("/getShellBase")
    public JsonResult getShellBase(@RequestParam(value = "shellDescribe", required = false) String shellDescribe) {
        List<ShellBase> list = shellBaseService.list(new LambdaQueryWrapper<ShellBase>().eq(StringUtils.isNotBlank(shellDescribe), ShellBase::getShellDescribe, shellDescribe));
        return JsonResult.success(list);
    }

    @ApiOperation("修改linux执行分组")
    @PostMapping("/updateShellBase")
    public JsonResult updateShellBase(@RequestBody ShellBaseVO vo) {
        ModelMapper mapper = new ModelMapper();
        ShellBase shellBase = mapper.map(vo, ShellBase.class);
        shellBase.setUpdateTime(LocalDateTime.now());
        shellBaseService.updateById(shellBase);
        return JsonResult.success();
    }

    @ApiOperation("删除linux执行分组")
    @PostMapping("/delShellBase")
    public JsonResult delShellBase(@RequestParam(value = "id") Long id) {
        shellBaseService.removeById(id);
        return JsonResult.success();
    }

    @ApiOperation("执行shell命令")
    @GetMapping("/executeShell")
    public JsonResult executeShell(@RequestParam(value = "shellBaseId") Long shellBaseId, @RequestParam(value = "ips") List<String> ips) {
        if (shellBaseId == null || CollectionUtils.isEmpty(ips)) {
            return JsonResult.fail();
        }
        ArrayList<ShellExecuteLog> shellExecuteLogs = new ArrayList<>();
        ips.forEach(v -> {
            ShellExecuteLog shellExecuteLog = new ShellExecuteLog();
            shellExecuteLog.setShellHost(v);
            shellExecuteLog.setShellBaseId(shellBaseId);
            shellExecuteLog.setCreateTime(LocalDateTime.now());
            shellExecuteLog.setUpdateTime(LocalDateTime.now());
            shellExecuteLogs.add(shellExecuteLog);
        });
        shellExecuteLogService.saveBatch(shellExecuteLogs);
        ThreadUtil.execAsync(()->executeCommand(shellBaseId,shellExecuteLogs));
        log.info("异步");
        return JsonResult.success();
    }

    @ApiOperation("获取执行shell记录")
    @GetMapping("/getExecuteCommand")
    public BasePageResponse<ShowShellExecute> getExecuteCommand(ExecuteCommandVo vo) {
        IPage<ShowShellExecute> page = new Page(vo.getPageNumber(), vo.getPageSize());
        QueryWrapper<ShellBase> wrapper = new QueryWrapper<ShellBase>().eq(StringUtils.isNotBlank(vo.getIp()), "a.shell_host", vo.getIp())
                .eq(StringUtils.isNotBlank(vo.getShellDescribe()), "b.shell_describe", vo.getShellDescribe())
                .orderByDesc("a.create_time")
                .between(vo.getStartTime() != null && vo.getEndTime() != null, "a.create_time", vo.getStartTime(), vo.getEndTime());
        page = shellBaseMapper.selectPages(page, wrapper);
        BasePageResponse<ShowShellExecute> pageResponse = new BasePageResponse<>();
        pageResponse.setPages(page.getPages());
        pageResponse.setCurrent(page.getCurrent());
        pageResponse.setSize(page.getSize());
        pageResponse.setTotal(page.getTotal());
        pageResponse.setRecord(page.getRecords());
        pageResponse.getRecord().forEach(v -> {
            //0 未执行 1成功 2 失败
            v.setStatusVal("未执行");
            if (v.getStatus() == 1) {
                v.setStatusVal("成功");
            }
            if (v.getStatus() == 2) {
                v.setStatusVal("失败");
            }
        });
        return pageResponse;

    }

    @Async
    public void executeCommand(Long shellBaseId, ArrayList<ShellExecuteLog> shellExecuteLogs) {
        ShellBase shellBase = shellBaseService.getById(shellBaseId);
        shellExecuteLogs.forEach(v -> {
            executeCommand(shellBase, v.getShellHost(), v.getId());
        });
    }


    public void executeCommand(ShellBase shellBase, String ip, Long id) {
        try {
            String user = shellBase.getShellUserName();
            String password = shellBase.getShellUserPassword();
            String command = shellBase.getShellCommand();
            JSch jsch = new JSch();
            Session session = null; // 默认SSH端口号为22
            session = jsch.getSession(user, ip, 22);
            session.setConfig("StrictHostKeyChecking", "no"); // 不进行公钥验证
            session.setPassword(password);
            session.connect();
            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command.getBytes(Charset.defaultCharset()));
            channel.setInputStream(null);
            channel.setErrStream(System.err);
            channel.connect();
            Thread.sleep(30000L);
            channel.disconnect();
            session.disconnect();
            shellExecuteLogService.update(new UpdateWrapper<ShellExecuteLog>()
                    .eq("id", id)
                    .set("status", 1)
                    .set("update_time", LocalDateTime.now()));
        } catch (Exception e) {
            shellExecuteLogService.update(new UpdateWrapper<ShellExecuteLog>()
                    .eq("id", id)
                    .set("status", 2)
                    .set("update_time", LocalDateTime.now()));
        }

    }
}
