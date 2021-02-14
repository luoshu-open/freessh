package org.freessh.sshclient.config;

import org.freessh.sshclient.model.SSHServer;
import org.freessh.sshclient.model.ServerParent;
import org.freessh.sshclient.util.Assert;
import org.freessh.sshclient.util.CollectionUtil;
import org.freessh.sshclient.util.Json;
import org.freessh.sshclient.util.StringUtil;
import org.freessh.terminal.util.IOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 配置项
 *
 * @author 朱小杰
 */
public class ConfigHelper {
    private Logger logger = LoggerFactory.getLogger(ConfigHelper.class);

    private ConfigHelper(){

    }

    private static ConfigHelper configHelper = new ConfigHelper();




    public static ConfigHelper getInstance(){
        return configHelper;
    }

    public synchronized void addServer(SSHServer server){
        this.addServer(null , server);
    }

    /**
     * 获取已配置的服务器列表
     * @return
     */
    public synchronized List<SSHServer> getServerList(){
        List<SSHServer> serverList = getConfig().getServerList();
        return serverList;
    }

    /**
     * 新增一个远程服务
     * @param parentId 父节点 id ， 如果为 null ， 则建立在根节点
     * @param server 服务器信息
     */
    public synchronized void addServer(String parentId , SSHServer server){
        Config config = getConfig();
        List<SSHServer> serverList = config.getServerList();
        if(serverList == null){
            serverList = new LinkedList<>();
            config.setServerList(serverList);
        }

        if(StringUtil.isBlank(parentId)){
            // 添加到根节点
            serverList.add(server);
        }else{
            // 需要找到对应的父节点，再做添加
            ServerParent serverParent = findById_(parentId, serverList, null);
            if(serverParent == null){
                String errMsg = "保存时未找到父节点 : " + parentId;
                logger.error(errMsg);
                throw new RuntimeException(errMsg);
            }
            SSHServer parent = serverParent.getServer();
            List<SSHServer> children = parent.getChildren();
            if(CollectionUtil.isEmpty(children)){
                children = new LinkedList<>();
                parent.setChildren(children);
            }
            children.add(server);
        }

        this.writeConfig(config);

    }


    public synchronized void deleteServer(String id) {
        Config config = getConfig();
        List<SSHServer> serverList = config.getServerList();
        deleteServer_(id , serverList);

        this.writeConfig(config);
    }

    private synchronized void deleteServer_(String id , List<SSHServer> serverList){
        Assert.assertNotBlank(id , "id 为空");
        Assert.assertNotEmpty(serverList , "服务列表为空");

        Iterator<SSHServer> iterator = serverList.iterator();
        while (iterator.hasNext()){
            SSHServer server = iterator.next();
            if(id.equals(server.getId())){
                iterator.remove();
            }
            if(!CollectionUtil.isEmpty(server.getChildren())){
                deleteServer_(id , server.getChildren());
            }
        }
    }

    public synchronized void updateServer(SSHServer server){
        Assert.assertNotNull(server , "信息为空");
        Assert.assertNotBlank(server.getId() , "修改的 id 为空");

        Config config = getConfig();
        List<SSHServer> serverList = config.getServerList();

        ServerParent serverParent = findById_(server.getId(), serverList, null);
        if(serverParent == null){
            String errMsg = "找不到对应的服务器";
            logger.error(errMsg);
            throw new RuntimeException(errMsg);
        }

        // 是否删除
        int findIndex = -1;

        SSHServer parent = serverParent.getParent();
        List<SSHServer> children = parent.getChildren();
        for (int i = 0; i < children.size(); i++) {
            SSHServer sshServer = children.get(i);
            if(server.getId().equals(sshServer.getId())){
                findIndex = i;
                break;
            }
        }

        if(findIndex == -1){
            String errMsg = "编辑失败，未找到编辑数据";
            logger.error(errMsg);
            throw new RuntimeException(errMsg);
        }
        children.remove(findIndex);
        children.add(findIndex , server);

        this.writeConfig(config);
    }

    public synchronized SSHServer findById(String id){
        Assert.assertNotBlank(id , "id 为空");
        Config config = getConfig();
        List<SSHServer> serverList = config.getServerList();
        if(CollectionUtil.isEmpty(serverList)){
            return null;
        }

        ServerParent serverParent = findById_(id, serverList, null);
        if(serverParent != null){
            return serverParent.getServer();
        }else{
            return null;
        }
    }

    private synchronized ServerParent findById_(String id , List<SSHServer> serverList , SSHServer parent){
        Assert.assertNotBlank(id , "id 为空");
        Assert.assertNotEmpty(serverList , "服务列表为空");
        for (SSHServer server : serverList) {
            if(id.equals(server.getId())){
                return new ServerParent(parent , server);
            }
            if(!CollectionUtil.isEmpty(server.getChildren())){
                return findById_(id , server.getChildren() , server);
            }

        }
        return null;
    }



    private synchronized Config getConfig(){
        Config config = null;

        String configPath = getConfigPath();
        String text = null;
        try(FileInputStream inputStream = new FileInputStream(configPath)){
            text = IOUtil.toString(inputStream);
        }catch (IOException e){
            String errMsg = "读取配置文件失败 ， " + e.getMessage();
            logger.error(errMsg);
            throw new RuntimeException(errMsg , e);
        }

        if(text == null || text.trim().equals("")){
            config = new Config();
            config.setInitTime(new Date());
        }else{
            config = Json.parse(text , Config.class);
        }
        return config;
    }

    private synchronized void writeConfig(Config config){
        String text = Json.toJsonString(config);

        String configPath = getConfigPath();
        try(OutputStream out = new FileOutputStream(configPath)){
            byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
            out.write(bytes);
        }catch (IOException e){
            String errMsg = "写入配置文件错误 , " + e.getMessage();
            logger.error(errMsg , e);
            throw new RuntimeException(errMsg , e);
        }
    }


    private synchronized String getConfigPath(){
        String userHome = System.getProperty("user.home");
        String sshConfigDir = userHome + "/freessh";
        String dbPath = sshConfigDir + "/freessh.db";
        File f = new File(sshConfigDir);
        if(!f.exists()){
            boolean r = f.mkdirs();
            if(!r){
                String errMsg = "初始化配置文件失败，可能是权限问题";
                logger.error(errMsg);
                throw new RuntimeException(errMsg);
            }
        }
        File dbFile = new File(dbPath);
        if(!dbFile.exists()){
            String errMsg = "初始化配置文件失败， 创建文件失败";
            try {
                boolean r = dbFile.createNewFile();
                if(!r){
                    logger.error(errMsg);
                    throw new RuntimeException(errMsg);
                }
            } catch (IOException e) {
                logger.error(errMsg , e);
                throw new RuntimeException(errMsg , e);
            }

        }
        return dbPath;
    }


}
