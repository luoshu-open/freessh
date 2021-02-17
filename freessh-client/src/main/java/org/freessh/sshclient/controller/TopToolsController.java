package org.freessh.sshclient.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.freessh.sshclient.component.ServerEdit;
import org.freessh.sshclient.component.Window;
import org.freessh.sshclient.util.WindowUtil;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * 时间： 2021/2/16 - 19:50
 *
 * @author 朱小杰
 */
public class TopToolsController implements Initializable {




    public TopToolsController() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    public void onAdd(MouseEvent e){
        Stage stage = new Stage();
        WindowUtil.setIcon(stage);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setWidth(400);
        stage.setHeight(500);

        ServerEdit serverEdit = new ServerEdit();
        serverEdit.setStage(stage);

        serverEdit.setBackground(WindowUtil.createBackground(Color.AZURE));
        Window window = new Window(serverEdit);
        window.setStyle("-fx-border-radius: 30;-fx-background-color: red;-fx-border-width: 1 1 1 1;");
        window.setBackground(WindowUtil.createBackground(Color.RED));

        Scene scene = new Scene(window);
        stage.setScene(scene);

        stage.show();
    }
}
