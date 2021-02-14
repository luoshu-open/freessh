package org.freessh.sshclient.test;
import org.freessh.sshclient.config.ConfigHelper;
import org.freessh.sshclient.model.SSHServer;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * @author 朱小杰
 */
public class ConfigTest {

    @Test
    public void writeConfig(){
        ConfigHelper helper = ConfigHelper.getInstance();

        SSHServer server = new SSHServer();
        server.setId(UUID.randomUUID().toString());
        server.setAlias("阿里云服务器");
        server.setHost("1111");
        server.setPort(22);
        server.setUsername("root");
        server.setPassword(Base64.getEncoder().encodeToString("111".getBytes(StandardCharsets.UTF_8)));
        server.setAuthType(0);
        server.setCreateTime(new Date());

        helper.addServer(server);
    }

    @Test
    public void writeWithParent(){

    }
}
