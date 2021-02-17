package org.freessh.sshclient.component;
import java.nio.charset.StandardCharsets;
import java.util.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.freessh.sshclient.config.ConfigHelper;
import org.freessh.sshclient.model.SSHServer;

import javax.xml.soap.Text;
import java.io.IOException;
import java.net.URL;

/**
 * 时间： 2021/2/17 - 7:56
 *
 * @author 朱小杰
 */
public class ServerEdit extends VBox implements Initializable {

    @FXML
    private TextField calias;

    @FXML
    private TextField chost;

    @FXML
    private TextField cport;

    @FXML
    private TextField cusername;

    @FXML
    private TextField cpassword;

    @FXML
    private TextArea cremark;

    private Stage stage;

    public ServerEdit() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/serveredit/ServerEdit.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println();
    }

    @FXML
    public void save(MouseEvent e){
        final ConfigHelper helper = ConfigHelper.getInstance();
        final SSHServer sshServer = new SSHServer();
        sshServer.setId(UUID.randomUUID().toString());
        sshServer.setAlias(this.calias.getText());
        sshServer.setHost(this.chost.getText());
        sshServer.setPort(Integer.parseInt(this.cport.getText()));
        sshServer.setUsername(this.cusername.getText());
        sshServer.setPassword(Base64.getEncoder().encodeToString(cpassword.getText().getBytes(StandardCharsets.UTF_8)));
        sshServer.setAuthType(0);
        sshServer.setRemark(this.cremark.getText());
        sshServer.setCreateTime(new Date());
        helper.addServer(sshServer);

        // 刷新服务器列表

        // 关闭窗口
        if(this.stage != null){
            this.stage.close();
        }

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
