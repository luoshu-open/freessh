package org.freessh.sshclient.component;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.freessh.sshclient.util.WindowUtil;

/**
 * 时间： 2021/2/17 - 8:45
 *
 * @author 朱小杰
 */
public class Window extends VBox {

    private int barHeight = 30;
    private Color barColor = Color.web("#ccc");

    private Pane body;


    public Window(Pane body) {
        this.body = body;

        this.prefHeightProperty().addListener((observable, oldValue, newValue) -> body.setPrefWidth(newValue.doubleValue() - barHeight));
        this.body.prefWidthProperty().bind(this.widthProperty());

        final Pane bar = createBar();

        getChildren().add(bar);
        getChildren().add(body);
    }

    /**
     * 创建标题栏
     * @return
     */
    protected Pane createBar(){
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.prefWidthProperty().bind(this.prefWidthProperty());
        hBox.setPrefHeight(this.barHeight);
        hBox.setBackground(WindowUtil.createBackground(barColor));

        Image image = new Image(getClass().getResourceAsStream("/img/cha.png"));
        ImageView close = new ImageView(image);
        close.setFitWidth(20);
        close.setFitHeight(20);

        hBox.getChildren().add(close);

        return hBox;
    }
}
