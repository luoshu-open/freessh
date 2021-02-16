package org.freessh.sshclient.component.tab;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lombok.Setter;
import org.freessh.sshclient.util.WindowUtil;

/**
 * 标签头,分为两部分，第一部分是文字内容，第二部分是动态显示X图标
 *
 * @author 朱小杰
 */
public class TabHeader extends HBox {

    private Color defaultColor = Color.web("#DFDFDF");
    private Color activeColor = Color.web("#F9F9F9");
    private Color hoverColor = Color.web("#F9F9F9");

    /**
     * 当前选项卡是否选中
     */
    private boolean selected = false;

    private final Image normalCloseImg = new Image(TabHeader.class.getResourceAsStream("/img/cha.png"));
    private final Image hoverCloseImg = new Image(TabHeader.class.getResourceAsStream("/img/cha2.png"));


    @Setter
    private EventHandler<MouseEvent> closeEvent;

    private HBox content;
    private HBox closeBtn;
    private ImageView closeImage;



    public TabHeader() {
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(1 , 10 , 1 , 10));
        this.setSpacing(10);

        this.content = new HBox();
        getChildren().add(this.content);

        this.closeImage = new ImageView(this.normalCloseImg);
        this.closeImage.setFitWidth(13);
        this.closeImage.setFitHeight(13);

        this.closeBtn = new HBox();
        this.closeBtn.setAlignment(Pos.CENTER);
        this.closeBtn.getChildren().add(this.closeImage);
        this.getChildren().add(this.closeBtn);


        // 关闭按钮移上去变化样式
        this.closeImage.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            this.closeImage.setImage(this.hoverCloseImg);
        });
        this.closeImage.addEventHandler(MouseEvent.MOUSE_EXITED , e -> {
            this.closeImage.setImage(this.normalCloseImg);
        });

        // 关闭按钮的点击事件
        this.closeBtn.addEventHandler(MouseEvent.MOUSE_CLICKED , e -> {
            e.consume();
            if(this.closeEvent != null){
                this.closeEvent.handle(e);
            }
        });

        // 鼠标移入移出的效果
        this.addEventHandler(MouseEvent.MOUSE_ENTERED , e -> {
            if(!this.selected){
//                this.showCloseButton();
                this.setBackground(WindowUtil.createBackground(this.hoverColor));
            }
        });
        this.addEventHandler(MouseEvent.MOUSE_EXITED , event -> {
            if(!this.selected){
//                this.hideCloseButton();
                this.setBackground(WindowUtil.createBackground(defaultColor));
            }
        });
    }


    /**
     * 把当前选项卡标记为激活
     */
    protected void select(){
        this.selected = true;
        this.setBackground(WindowUtil.createBackground(this.activeColor));
//        this.content.setBackground(WindowUtil.createBackground(this.activeColor));
    }

    /**
     * 当前是否激活
     * @return
     */
    public boolean isSelect(){
        return this.selected;
    }

    /**
     * 把选项卡取消激活
     */
    protected void cancelSelect(){
        this.selected = false;
        this.setBackground(WindowUtil.createBackground(this.defaultColor));
    }

    public void setContent(Pane pane){
        this.content.getChildren().clear();
        this.content.getChildren().add(pane);
    }


    public void hideCloseButton(){
        this.getChildren().remove(this.closeBtn);
    }
    public void showCloseButton(){
        this.hideCloseButton();
        this.getChildren().add(this.closeBtn);
    }
}
