package org.freessh.sshclient.component.tab;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import lombok.Data;
import lombok.Setter;

/**
 * @author 朱小杰
 */
@Data
public class Tab {

    /**
     * 头信息
     */
    private TabHeader header;


    /**
     * 主内容
     */
    private Node body;


    private EventHandler<MouseEvent> clickEvent;
    /**
     * 点击关闭按钮触发的事件
     */
    @Setter
    private EventHandler<MouseEvent> closeEvent;



    public Tab(Pane header, Node body) {
        this.body = body;

        Pane tabPane = createHeaderPane();
        tabPane.getChildren().add(header);

        this.header = new TabHeader();


        this.header.setContent(tabPane);

        this.header.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(clickEvent != null){
                   clickEvent.handle(event);
                }
            }
        });
        // 注册关闭事件
        this.header.setCloseEvent(e -> {
            if(this.closeEvent != null){
                this.closeEvent.handle(e);
            }
        });
    }

    /**
     * 创建一个选项卡的 pane
     */
    protected Pane createHeaderPane(){
        HBox pane = new HBox();
        pane.setPrefHeight(TabView.headerHeight);
        pane.setAlignment(Pos.CENTER);
//        pane.setBackground(WindowUtil.createBackground(TabView.headerColor));
        return pane;
    }

    /**
     * 注册一个点击事件
     * @param e
     */
    public void onClick(EventHandler<MouseEvent> e){
        this.clickEvent = e;
    }

    public boolean isSelect(){
        return this.header.isSelect();
    }




    public Node getBody(){
        return this.body;
    }
}
