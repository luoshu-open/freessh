package org.freessh.sshclient;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.freessh.sshclient.component.grouplist.GroupListView;
import org.freessh.sshclient.component.ImageButton;
import org.freessh.sshclient.component.grouplist.Item;
import org.freessh.sshclient.component.terminal.TerminalView;
import org.freessh.sshclient.config.ConfigHelper;
import org.freessh.sshclient.model.SSHServer;
import org.freessh.sshclient.util.CollectionUtil;
import org.freessh.sshclient.util.Constant;
import org.freessh.sshclient.util.WindowUtil;
import org.freessh.terminal.Terminal;
import org.freessh.terminal.model.SSHConnectConfig;

import java.util.Base64;
import java.util.List;

/**
 * @author 朱小杰
 */
public class App extends Application {

    private BorderPane mainContainer;
    private TerminalView terminalView;

    @Override
    public void start(Stage primaryStage) throws Exception {
        WindowUtil.setIcon(primaryStage);


        initMainUI(primaryStage);

        primaryStage.show();
    }


    /**
     * 渲染主界面UI。
     * 上面为工具栏
     * 左侧为服务器列表
     * 中间为命令终端
     * 右侧也为工具栏
     * 下侧为文件传输栏
     *
     * @param stage
     */
    private void initMainUI(Stage stage) {
        this.mainContainer = new BorderPane();
        mainContainer.setPrefSize(100, 600);

        Pane left = createServerListUI();
        this.terminalView = createTerminalView();
        Pane topTools = createTopTools();
        topTools.prefWidthProperty().bind(mainContainer.widthProperty());


        mainContainer.setLeft(left);
        mainContainer.setCenter(terminalView);
        mainContainer.setTop(topTools);


        Scene scene = new Scene(mainContainer, 1000, 600);

        stage.setScene(scene);

    }


    /**
     * 创建顶部工具栏
     *
     * @return
     */
    protected Pane createTopTools() {
        HBox pane = new HBox();
        pane.setPrefHeight(40);
        pane.setPadding(new Insets(0 , 15 , 0 , 15));
        pane.setBackground(WindowUtil.createBackground(Constant.BACKGROUND_COLOR));

        pane.setStyle("-fx-border-width: 0 0 1 0;-fx-border-color: #ccc;");

        ImageButton imageButton = new ImageButton("/img/add.png" , "增加");
        pane.getChildren().add(imageButton);

        return pane;
    }

    protected TerminalView createTerminalView() {
        return new TerminalView();
    }

    /**
     * 创建左侧服务器列表
     *
     * @return
     */
    protected Pane createServerListUI() {
        Pane pane = new Pane();
        pane.setPrefWidth(200);
        GroupListView groupListView = new GroupListView();

        groupListView.prefHeightProperty().bind(pane.heightProperty());
        groupListView.prefWidthProperty().bind(pane.widthProperty());

        ConfigHelper helper = ConfigHelper.getInstance();
        List<SSHServer> serverList = helper.getServerList();
        if (!CollectionUtil.isEmpty(serverList)) {
            for (SSHServer server : serverList) {
                // TODO: 这里没有递归考虑分组的情况
                Item item = new Item(server.getHost(), server.getAlias(), server);
                item.addEventHandler(MouseEvent.MOUSE_CLICKED , e -> {
                    int clickCount = e.getClickCount();
                    if(clickCount == 2){
                        // 双击事件 , 双击时连接终端
                        Item isource = (Item) e.getSource();
                        SSHServer source = (SSHServer) isource.getData();
                        SSHConnectConfig sshConnectConfig = new SSHConnectConfig();
                        sshConnectConfig.setType(source.getAuthType());
                        sshConnectConfig.setHost(source.getHost());
                        sshConnectConfig.setPort(source.getPort());
                        sshConnectConfig.setUsername(source.getUsername());
                        sshConnectConfig.setPassword(new String(Base64.getDecoder().decode(source.getPassword())));

                        Terminal terminal = new Terminal(sshConnectConfig);
                        this.terminalView.addTerminal(terminal);
                    }
                });
                groupListView.getChildren().add(item);
            }
        }
        pane.getChildren().add(groupListView);

        pane.setBackground(WindowUtil.createBackground(Constant.BACKGROUND_COLOR2));

        return pane;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
