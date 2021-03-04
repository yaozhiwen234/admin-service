package com.yzw.utils;

import java.io.*;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import com.alibaba.fastjson.util.IOUtils;
import lombok.extern.slf4j.Slf4j;


/**
 * @author yzw
 * @date 2021/3/3
 */
@Slf4j
public class SSH {

    private Connection conn;
    private String ip;
    private String username;
    private String password;
    private static final int TIME_OUT = 0;// 表示不超时
    private static String DEFAULTCHART = "UTF-8";

    /**
     * 构造函数
     *
     * @param ip       远程ip
     * @param username 远程机器用户名
     * @param password 远程机器密码
     */
    public SSH(String ip, String username, String password) {
        this.ip = ip;
        this.username = username;
        this.password = password;
    }


    /**
     * 登录
     *
     * @return
     * @throws IOException
     */
    private boolean login() throws IOException {
        conn = new Connection(ip);
        conn.connect();
        return conn.authenticateWithPassword(username, password);
    }

    /**
     * 执行脚本
     *
     * @param shell
     * @return
     * @throws Exception
     */
    public Boolean exec(String shell) {
        String result = "";
        try {
            if (login()) {
                Session session = conn.openSession();
                session.execCommand(shell);
                session.waitForCondition(ChannelCondition.EXIT_STATUS, TIME_OUT);
                result = processStdout(session.getStdout(), DEFAULTCHART);


                // 如果为输出为空，说明脚本执行出错了
                if (StringUtils.isBlank(result)) {
                    log.error(processStdout(session.getStderr(), DEFAULTCHART));
                    return false;
                } else {
                    log.info(result);
                    return true;
                }

            } else {
                throw new Exception("登录远程机器失败" + ip); // 自定义异常类 实现略
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("异常脚本" + e);
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return false;
    }

    /**
     * 解析脚本执行返回的结果集
     *
     * @param in      输入流对象
     * @param charset 编码
     * @return 以纯文本的格式返回
     * @throws Exception
     * @since V0.1
     */
    private String processStdout(InputStream in, String charset) throws Exception {
        InputStream stdout = new StreamGobbler(in);
        StringBuffer buffer = new StringBuffer();
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            isr = new InputStreamReader(stdout, charset);
            br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line + "\n");
            }
        } catch (UnsupportedEncodingException e) {
            throw new Exception("不支持的编码字符集异常", e);
        } catch (IOException e) {
            throw new Exception("读取指纹失败", e);
        } finally {
            IOUtils.close(br);
            IOUtils.close(isr);
            IOUtils.close(stdout);
        }
        return buffer.toString();
    }

    public static void main(String[] args) {
        try {
            SSH executor = new SSH("118.25.6.5", "root", "Yzw199850");
            // sh /user/adminApp/script/creatFile.sh
            executor.exec("sh /user/adminApp/script/creatFile.sh");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
