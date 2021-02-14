package org.freessh.sshclient.component.tab;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import lombok.Data;

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



    public Tab(Pane header, Node body) {
        this.body = body;
        this.header = new TabHeader();


        this.header.setContent(header);

        this.header.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(clickEvent != null){
                   clickEvent.handle(event);
                }
            }
        });
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
