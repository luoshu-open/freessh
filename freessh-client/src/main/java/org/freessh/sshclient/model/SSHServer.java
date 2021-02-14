package org.freessh.sshclient.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * ssh 服务器的连接信息
 *
 * @author 朱小杰
 */
@Data
public class SSHServer {

    /**
     * 服务器的 id
     */
    private String id;

    /**
     * 服务器的别名
     */
    private String alias;

    /**
     * 服务器的地址
     */
    private String host;

    private int port;

    private String username;

    /**
     * 连接密码
     */
    private String password;


    /**
     * 认证类型， 1 用户密码 ， 2 证书
     */
    private int authType;

    /**
     * 创建时间
     */
    @JsonProperty()
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 如果这里有值，说明这个是一个分组，那么这里除了 alias 和 id ， 所有的属性都应该为空
     */
    private List<SSHServer> children;

}
