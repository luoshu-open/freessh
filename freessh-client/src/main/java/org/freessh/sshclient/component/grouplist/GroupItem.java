package org.freessh.sshclient.component.grouplist;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.InputStream;


/**
 * @author 朱小杰
 */
public class GroupItem extends VBox {

    /**
     * 分组标题
     */
    private String title;
    private Text titleText;

    private final VBox detail = new VBox();


    public GroupItem() {
        this("分组");
    }

    public GroupItem(String title){
        this.title = title;
        this.titleText = new Text(title);

//        detail.setSpacing(10);

        Node header = getHeader();

        getChildren().add(header);
        getChildren().add(this.detail);
    }


    /**
     * 获取分组的头部
     * @return
     */
    protected Node getHeader(){
        HBox header = new HBox();
//        header.setCursor(Cursor.HAND);
        ImageView imageView = new ImageView(getDirIcon());
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
        header.getChildren().add(imageView);
        header.getChildren().add(this.titleText);
        header.setAlignment(Pos.CENTER_LEFT);
        return header;
    }

    /**
     * 设置分组标题
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
        this.titleText.setText(title);
    }

    /**
     * 增加节点，只能添加 {@link Item} 和 {@link GroupItem}
     * @param node
     */
    public void addItem(Node node){
        if(node == null){
            throw new NullPointerException("node is null");
        }
        if(!(node instanceof Item) && !(node instanceof GroupItem)){
            throw new RuntimeException("只能添加 Item 或 GroupItem");
        }

        Pane empty = new Pane();
        empty.setPrefWidth(10);
        empty.setPrefHeight(1);
        ((Pane) node).getChildren().add(0 , empty);
        this.detail.getChildren().add(node);

    }


    /**
     * 获取文件夹的图标
     * @return
     */
    protected Image getDirIcon(){
        InputStream inputStream = GroupItem.class.getResourceAsStream("/img/dir.png");
        Image image = new Image(inputStream);
        return image;
    }
}
