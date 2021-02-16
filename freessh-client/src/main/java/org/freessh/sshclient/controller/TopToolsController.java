package org.freessh.sshclient.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * 时间： 2021/2/16 - 19:50
 *
 * @author 朱小杰
 */
public class TopToolsController implements Initializable {

    private int height = 40;

    public TopToolsController() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    public void onAdd(MouseEvent e){
        System.out.println(e);
    }
}
