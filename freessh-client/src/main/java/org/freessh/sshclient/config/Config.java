package org.freessh.sshclient.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.freessh.sshclient.model.SSHServer;

import java.util.Date;
import java.util.List;

/**
 * @author 朱小杰
 */
@Data
public class Config {

    /**
     * 初始化时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date initTime;

    /**
     * 服务器列表
     */
    private List<SSHServer> serverList;
}
