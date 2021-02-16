package org.freessh.sshclient.component.topbar;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

import java.io.IOException;

/**
 * 时间： 2021/2/16 - 20:28
 *
 * @author 朱小杰
 */
public class TopTools extends HBox {

    public TopTools() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TopTools.fxml"));
        // 把 fxml 的内容，赋予给自己
        loader.setRoot(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage() , e);
        }
    }
}
