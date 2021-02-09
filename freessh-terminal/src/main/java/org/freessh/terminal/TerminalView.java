package org.freessh.terminal;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebErrorEvent;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import org.freessh.terminal.util.ThreadHelper;

import java.io.Reader;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * 终端
 *
 * @author 朱小杰
 */
public class TerminalView extends Pane {

    private ObjectProperty<String> a;

    private final WebView webView;
    private final WebEngine webEngine;

    /**
     * 控制台窗口的列数
     */
    private final ReadOnlyIntegerWrapper columnsProperty;
    /**
     * 控制台窗口的行数
     */
    private final ReadOnlyIntegerWrapper rowsProperty;

    /**
     * 输入流
     */
    private final ObjectProperty<Reader> inputReaderProperty;
    /**
     * 错误流
     */
    private final ObjectProperty<Reader> errorReaderProperty;

    /**
     * web 终端的配置项
     */
    private TerminalConfig terminalConfig = new TerminalConfig();
    protected final CountDownLatch countDownLatch = new CountDownLatch(1);




    public TerminalView() {
        this.webView = new WebView();
        this.webEngine = this.webView.getEngine();

        webEngine.onErrorProperty().addListener(new ChangeListener<EventHandler<WebErrorEvent>>() {
            @Override
            public void changed(ObservableValue<? extends EventHandler<WebErrorEvent>> observable, EventHandler<WebErrorEvent> oldValue, EventHandler<WebErrorEvent> newValue) {
                System.out.println();
            }
        });

        // 初始化控制台窗口的列数与行数
        this.columnsProperty = new ReadOnlyIntegerWrapper(150);
        this.rowsProperty = new ReadOnlyIntegerWrapper(10);

        // 初始化流信息
        inputReaderProperty = new SimpleObjectProperty<>();
        errorReaderProperty = new SimpleObjectProperty<>();

        inputReaderProperty.addListener((observable, oldValue, newValue) -> {
            ThreadHelper.start(() -> {
                printReader(newValue);
            });
        });

        errorReaderProperty.addListener((observable, oldValue, newValue) -> {
            ThreadHelper.start(() -> {
                printReader(newValue);
            });
        });

        // 把当前对象绑定到 js 对象中
        this.webEngine.getLoadWorker().stateProperty().addListener((observable , oldValue , newValue) -> {
            // 把当前变量，绑定到 js 的 window对象的 app 中，后面可以这样使用 window.app.xxx() , 这里的 xxx 对应的是 java 的方法
            getWindow().setMember("app" , this);
        });
        // 绑定 webview 窗口的大小，猜测是放大缩小窗口时，会自动变化大小
        this.webView.prefHeightProperty().bind(heightProperty());
        this.webView.prefWidthProperty().bind(widthProperty());

        // 加载页面
        this.webEngine.load(TerminalView.class.getResource("/xterm/hterm.html").toExternalForm());
//        this.getChildren().add(webView);
    }

    public void copy(String text) {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(text);
        clipboard.setContent(clipboardContent);
    }

    /**
     * 把 web 终端的配置项转换为 json
     * @return
     */
    public String getPrefs() {
        try {
            return new ObjectMapper().writeValueAsString(getTerminalConfig());
        } catch(final Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取终端的配置信息
     * @return
     */
    public TerminalConfig getTerminalConfig() {
        if(Objects.isNull(terminalConfig)) {
            terminalConfig = new TerminalConfig();
        }
        return terminalConfig;
    }

    /**
     * 设置终端的配置
     * @param terminalConfig
     */
    public void setTerminalConfig(TerminalConfig terminalConfig){
        this.terminalConfig = terminalConfig;
    }

    public void onTerminalInit() {
        ThreadHelper.runActionLater(() -> {
            getChildren().add(webView);
        }, true);
    }
    public void resizeTerminal(int columns, int rows) {
        columnsProperty.set(columns);
        rowsProperty.set(rows);
    }


    public void setInputReader(Reader reader) {
        inputReaderProperty.set(reader);
    }
    public void setErrorReader(Reader reader) {
        errorReaderProperty.set(reader);
    }

    /**
     * 获取行的属性
     * @return
     */
    public ReadOnlyIntegerProperty columnsProperty() {
        return columnsProperty.getReadOnlyProperty();
    }

    /**
     * 获取列的属性
     * @return
     */
    public ReadOnlyIntegerProperty rowsProperty() {
        return rowsProperty.getReadOnlyProperty();
    }

    /**
     * 打印 reader
     * @param bufferedReader
     */
    private void printReader(Reader bufferedReader) {
        try {
            int nRead;
            final char[] data = new char[1 * 1024];

            while((nRead = bufferedReader.read(data, 0, data.length)) != -1) {
                final StringBuilder builder = new StringBuilder(nRead);
                builder.append(data, 0, nRead);
                print(builder.toString());
            }

        } catch(final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 在终端中打印信息
     * @param text
     */
    protected void print(String text) {
        ThreadHelper.awaitLatch(countDownLatch);
        ThreadHelper.runActionLater(() -> {
            //获取 js 中终端的 io对象 ， 在终端中打印信息
            getTerminalIO().call("print", text);
        });

    }


    /**
     * 当终端准备好时触发
     */
    public void onTerminalReady() {
        ThreadHelper.start(() -> {
            try {
                focusCursor();
                countDownLatch.countDown();
            } catch(final Exception e) {
            }
        });
    }

    /**
     * 定位光标的位置
     */
    public void focusCursor() {
        ThreadHelper.runActionLater(() -> {
            webView.requestFocus();
            getTerminal().call("focus");
        }, true);
    }

    /**
     * 获取在 web 终端中的 io 流对象
     * @return
     */
    private JSObject getTerminalIO() {
        return (JSObject) webEngine.executeScript("t.io");
    }

    /**
     * 获取 js 中的 t 对象，这是在 js 中初始化的终端对象
     * @return
     */
    private JSObject getTerminal() {
        return (JSObject) webEngine.executeScript("t");
    }

    /**
     * 获取 js 的 window 对象
     * @return
     */
    public JSObject getWindow() {
        return (JSObject) webEngine.executeScript("window");
    }
}
