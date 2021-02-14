package org.freessh.sshclient.component;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.freessh.sshclient.util.Constant;
import org.freessh.sshclient.util.WindowUtil;

import java.io.InputStream;

/**
 * 图片按钮
 *
 * @author 朱小杰
 */
public class ImageButton extends VBox {

    /**
     * 字体颜色
     */
    private Color fontColor = Constant.BACKGROUND_FONT_COLOR;

    /**
     * 鼠标移上去的背景色
     */
    private Color hoverColor = Constant.BACKGROUND_HOVER_COLOR;

    private InputStream img;
    private String text;

    private ImageView imageView;
    private Text t;

    public ImageButton(InputStream img, String text) {
        this.img = img;
        this.text = text;

        this.renderUI();
    }

    public ImageButton(String imgPath, String text) {
        this(ImageButton.class.getResourceAsStream(imgPath), text);
    }

    private void renderUI() {
        this.setPadding(new Insets(5 , 10 , 5 , 10));
        Image image = new Image(this.img);
        this.imageView = new ImageView(image);
        this.imageView.setFitWidth(18);
        this.imageView.setFitHeight(18);

        this.t = new Text(this.text);
        this.t.setFont(Font.font(12));
        this.t.setFill(fontColor);

        this.getChildren().add(this.imageView);
        this.getChildren().add(this.t);


        this.setAlignment(Pos.CENTER);

        // 当鼠标移上去时，改变颜色
        this.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            this.setBackground(WindowUtil.createBackground(hoverColor));
        });
        this.addEventHandler(MouseEvent.MOUSE_EXITED , e -> {
            this.setBackground(null);
        });
    }

    /**
     * 设置字体颜色
     *
     * @param color
     */
    public void setFontColor(Color color) {
        this.t.setFill(color);
    }
}
