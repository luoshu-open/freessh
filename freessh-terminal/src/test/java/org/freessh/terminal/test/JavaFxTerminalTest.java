package org.freessh.terminal.test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.freessh.terminal.Terminal;

import java.io.IOException;

/**
 * @author 朱小杰
 */
public class JavaFxTerminalTest extends Application {
    public static void main(String... args)
            throws IOException {
        launch(args);


    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane pane = new Pane();
        pane.setPrefSize(400 , 400);
        Scene scene = new Scene(pane , 800 , 600);
        primaryStage.setScene(scene);

        Terminal terminal = new Terminal(null);

        pane.getChildren().add(terminal);

        terminal.prefWidthProperty().bind(pane.widthProperty());
        terminal.prefHeightProperty().bind(pane.heightProperty());

        primaryStage.show();
    }
}
