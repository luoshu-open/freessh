package org.freessh.sshclient.component;

import javafx.beans.NamedArg;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;

/**
 * 时间： 2021/2/17 - 18:56
 *
 * @author 朱小杰
 */
public class FormItem extends HBox {


    /**
     * 如果要在 fxml 中设置参数，则必须同时有 get/set 方法
     */
    @Getter
    private String text;
    private Text label;

    private int textWidth = 50;

    /**
     * 如果要在 fxml 中设置参数，则必须同时有 get/set 方法
     */
    @Getter
    private Region body;

    public FormItem() {
        this.setAlignment(Pos.CENTER_LEFT);
        this.label = new Text();
        this.setTextWidth(this.textWidth);
        getChildren().add(label);
    }

    public void setText(String text) {
        this.text = text;
        this.label.setText(text);
    }

    public void setBody(Region body) {
        this.body = body;
        if(getChildren().size() > 1){
            getChildren().remove(1);
        }
        getChildren().add(body);

    }

    public int getTextWidth() {
        return textWidth;
    }

    public void setTextWidth(int textWidth) {
        this.textWidth = textWidth;
        this.label.setWrappingWidth(textWidth);
    }
}

