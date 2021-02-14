package org.freessh.sshclient.util;

import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.InputStream;

/**
 * @author 朱小杰
 */
public class WindowUtil {


    /**
     * 设置窗口图标
     * @param stage
     */
    public static void setIcon(Stage stage) {
        InputStream inputStream = WindowUtil.class.getResourceAsStream("/img/mainicon.png");
        Image image = new Image(inputStream);
        stage.getIcons().add(image);
    }


    /**
     * 根据颜色，创建一个 background 对象
     * @param color
     * @return
     */
    public static Background createBackground(Color color) {
        BackgroundFill fill = new BackgroundFill(color , null , null);
        Background background = new Background(fill);

        return background;
    }

    /**
     * 设置鼠标移上去后，变化颜色
     */
    public static void setActiveColor(){

    }
}
