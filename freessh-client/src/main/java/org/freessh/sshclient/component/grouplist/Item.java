package org.freessh.sshclient.component.grouplist;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.freessh.sshclient.util.WindowUtil;

import java.io.InputStream;

/**
 * 项
 *
 * @author 朱小杰
 */
public class Item extends HBox {

    private Color itemBgColor = Color.web("#EDEFF1");
    /**
     * 鼠标移上去之后的颜色
     */
    private Color focusColor = Color.web("#6D8DC1");


    /**
     * 主要内容，包括 别名， 和 ip
     */
    private final VBox content = new VBox();

    /**
     * 主要文本
     */
    private Text masterText;
    /**
     * 副文本
     */
    private Text slaveText;

    private ImageView imageView;

    /**
     * 存储对象，可以存储任意对象
     */
    private Object obj;


    public Item(String masterText , String slaveText , Object obj){
        this();
        this.obj = obj;
        this.setMasterText(masterText);
        this.setSlaveText(slaveText);
    }

    public Item(String masterText , String slaveText){
        this();
        this.setMasterText(masterText);
        this.setSlaveText(slaveText);
    }

    public Item() {
        initActiveColor();
        this.setPrefHeight(40);
        this.setPadding(new Insets(7));
        this.setStyle("-fx-border-width: 1 0 0 0;-fx-border-color: #ccc;");


        // 第一个参数是设置颜色，第二个参数设置圆角，类似 css 中的 border-radius() ， 第三个参数设置内 padding
        BackgroundFill backgroundFill = new BackgroundFill(itemBgColor, null , null);
        Background background = new Background(backgroundFill);
        this.setBackground(background);

        this.imageView = getIcon();
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);

        this.getChildren().add(imageView);

        this.masterText = new Text();
        this.slaveText = new Text();
        this.slaveText.setFont(new Font(12));
        this.slaveText.setFill(Color.web("#666666"));

        this.content.setPadding(new Insets(0 , 0 , 0 , 10));
        this.content.getChildren().add(this.masterText);
        this.content.getChildren().add(this.slaveText);

        this.getChildren().add(this.content);

        // 图标和文字居中对齐
        this.setAlignment(Pos.CENTER_LEFT);
    }

    /**
     * 初始化鼠标移上去后的颜色变化
     */
    private void initActiveColor(){
        this.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            this.setBackground(WindowUtil.createBackground(focusColor));
        });
        this.addEventHandler(MouseEvent.MOUSE_EXITED , e -> {
            this.setBackground(WindowUtil.createBackground(itemBgColor));
        });

    }

    /**
     * 设置这个 item 对应的对象数据，可以存储任意数据
     * @param obj
     */
    public void setData(Object obj){
        this.obj = obj;
    }

    /**
     * 获取数据
     * @return
     */
    public Object getData(){
        return this.obj;
    }


    /**
     * 设置主文本
     * @param text
     */
    public void setMasterText(String text){
        this.masterText.setText(text);
    }

    /**
     * 设置副文本
     * @param text
     */
    public void setSlaveText(String text){
        this.slaveText.setText(text);
    }

    protected ImageView getIcon(){
        InputStream iconIn = Item.class.getResourceAsStream("/img/computer.png");
        Image image = new Image(iconIn);
        ImageView imageView = new ImageView(image);

        return imageView;
    }
}
