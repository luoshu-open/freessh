package org.freessh.sshclient.component.terminal;

import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.freessh.sshclient.component.tab.Tab;
import org.freessh.sshclient.component.tab.TabView;
import org.freessh.sshclient.util.StringUtil;
import org.freessh.terminal.Terminal;

/**
 * 终端的显示控件
 *
 * @author 朱小杰
 */
public class TerminalView extends Pane {

    /**
     * 当没有连接任何终端时，显示此 pane
     */
    private Pane emptyPane = new Pane();

    /**
     * 当有任意一个终端时，显示这个
     */
    private TabView tabView;

    /**
     * 当前是否显示空页面
     */
    private boolean empty = true;

    private Terminal currentTerminal;

    public TerminalView() {
        Text text = new Text("空的");
        emptyPane.getChildren().add(text);

        this.tabView = new TabView();
        this.initUI();
    }


    private void initUI(){


        this.emptyPane.prefWidthProperty().bind(this.widthProperty());
        this.emptyPane.prefHeightProperty().bind(this.heightProperty());

        this.tabView.prefWidthProperty().bind(this.widthProperty());
        this.tabView.prefHeightProperty().bind(this.heightProperty());

        if(this.tabView.getTabs().size() >= 1){
            if (this.empty) {
                this.empty = false;
                getChildren().removeAll();
                getChildren().add(tabView);
            }
        }else{
            if(!this.empty){
                this.empty = true;
                getChildren().removeAll();
                getChildren().add(emptyPane);
            }

        }
    }

    public void addTerminal(Terminal terminal) {
        if(this.currentTerminal != null){
            // 如果当前的正是此终端，则不处理
            if(this.currentTerminal.equals(terminal)){
                return;
            }
        }
        this.currentTerminal = terminal;

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(2);

        Text hostText = new Text("192.168.191.191");
        hostText.setFont(Font.font(14));
        hostText.setFill(Color.web("#333333"));
        vBox.getChildren().add(hostText);

        String alias = "IDC 机房真好看啊，好地方";
        if(!StringUtil.isBlank(alias)){
            if(alias.length() > 10){
                alias = alias.substring(0 , 10);
                alias = alias + "..";
            }
        }
        Text aliasText = new Text(alias);
        aliasText.setFont(Font.font(11));
        aliasText.setFill(Color.web("#777777"));
        vBox.getChildren().add(aliasText);

        Tab tab = new Tab(vBox , terminal);
        this.tabView.getTabs().add(tab);
        initUI();
    }

}
