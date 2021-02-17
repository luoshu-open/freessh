package org.freessh.sshclient.component;

import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;
import org.freessh.sshclient.util.WindowUtil;

/**
 * 时间： 2021/2/17 - 21:32
 *
 * @author 朱小杰
 */
public class SButton extends Button {

    @Getter
    private Type typ;


    public SButton() {
        this.setId("sbutton");
        this.setStyle("    -fx-border-radius: 10;-fx-padding: 6 20 6 20;\n" +
                "    -fx-effect: dropshadow(three-pass-box, #666, 2, 0, 0, 0);");
        this.setTyp(Type.DEFAULT);

    }


    public void setTyp(Type typ) {
        this.typ = typ;
        switch (typ){
            case DEFAULT:{
                this.setBackground(WindowUtil.createBackground(Color.web("#F1F1F1")));
                this.setTextFill(Color.web("#333"));
                break;
            }
            case PRIMARY:{
                this.setBackground(WindowUtil.createBackground(Color.web("#248dff")));
                this.setTextFill(Color.web("#fff"));
                break;
            }
        }
    }

    public static enum Type{
        DEFAULT,
        PRIMARY,
        DANGER,
        WARN
    }
}
