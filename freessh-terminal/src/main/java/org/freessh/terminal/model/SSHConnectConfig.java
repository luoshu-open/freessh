package org.freessh.terminal.model;

import lombok.Data;

/**
 * ssh 连接的配置
 *
 * @author 朱小杰
 */
@Data
public class SSHConnectConfig {

    /**
     * 0 代表用户密码登陆
     */
    private int type;

    private String host;

    private int port;

    private String username;

    private String password;


}
