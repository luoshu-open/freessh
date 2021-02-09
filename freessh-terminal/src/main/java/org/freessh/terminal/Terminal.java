package org.freessh.terminal;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import org.freessh.terminal.annotation.WebCall;
import org.freessh.terminal.util.ThreadHelper;

import java.io.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author 朱小杰
 */
public class Terminal extends TerminalView{

    /**
     * 使用一个队列来保存终端输入的命令，将会在另一个线程中将命令执行
     */
    private final LinkedBlockingQueue<String> commandQueue;

    private final ObjectProperty<Writer> outputWriterProperty;




    public Terminal() {
        this.commandQueue = new LinkedBlockingQueue<>();
        this.outputWriterProperty = new SimpleObjectProperty<>();
    }

    /**
     * js 调用
     * 终端输入的命令，会传入这个方法进行执行
     * @param command
     */
    @WebCall
    public void command(String command) {
        try {
            commandQueue.put(command);
        } catch (final InterruptedException e) {
            throw new RuntimeException(e);
        }
        ThreadHelper.start(() -> {
            try {
                final String commandToExecute = commandQueue.poll();
                getOutputWriter().write(commandToExecute);
                getOutputWriter().flush();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onTerminalReady() {
        ThreadHelper.start(() -> {
            try {
                // 当终端初始化后，开始连接终端
                initializeProcess();
            } catch (final Exception e) {

            }
        });
    }

    /**
     * 更新窗口大小
     */
    private void updateWinSize() {
        try {
//            process.setWinSize(new WinSize(getColumns(), getRows()));
        } catch (Exception e) {
            //
        }
    }

    /**
     * 设置输出流信息
     * @param writer
     */
    public void setOutputWriter(Writer writer) {
        outputWriterProperty.set(writer);
    }

    /**
     * 初始化 pty4j 终端
     */
    protected void initializeProcess(){
        // 当行列属性发生变化时，修改终端的大小
        columnsProperty().addListener(e -> updateWinSize());
        rowsProperty().addListener(e -> updateWinSize());

        // 第一次初始化终端大小
        updateWinSize();

//        setInputReader(new BufferedReader(new InputStreamReader(process.getInputStream())));
//        setErrorReader(new BufferedReader(new InputStreamReader(process.getErrorStream())));
//        setOutputWriter(new BufferedWriter(new OutputStreamWriter(process.getOutputStream())));

        ThreadHelper.start(() -> {
            try {
                sshConnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        focusCursor();

        countDownLatch.countDown();
    }

    public void sshConnect() throws IOException {

        final SSHClient ssh = new SSHClient();

        // 验证 ssh 的地址，使用 PromiscuousVerifier 为不验证
        ssh.addHostKeyVerifier(new PromiscuousVerifier());

        ssh.connect("101.37.34.225" , 22);
        try {
            ssh.authPassword("root" , "zxj123qwe!@#");
//            ssh.authPublickey(System.getProperty("user.name"));

            final Session session = ssh.startSession();
            try {

                session.allocateDefaultPTY();

                final Session.Shell shell = session.startShell();

//                setInputReader(new BufferedReader(new InputStreamReader(process.getInputStream())));
                setInputReader(new BufferedReader(new InputStreamReader(shell.getInputStream())));
//                setErrorReader(new BufferedReader(new InputStreamReader(process.getErrorStream())));
                setErrorReader(new BufferedReader(new InputStreamReader(shell.getErrorStream())));
//                setOutputWriter(new BufferedWriter(new OutputStreamWriter(process.getOutputStream())));
                setOutputWriter(new BufferedWriter(new OutputStreamWriter(shell.getOutputStream())));


//                new StreamCopier(shell.getInputStream(), new PrintStream(shell.getOutputStream()), LoggerFactory.DEFAULT)
//                        .bufSize(shell.getLocalMaxPacketSize())
//                        .spawn("stdout");
//
//                PrintStream errorPrint = new PrintStream(shell.getOutputStream());
//                new StreamCopier(shell.getErrorStream(), errorPrint, LoggerFactory.DEFAULT)
//                        .bufSize(shell.getLocalMaxPacketSize())
//                        .spawn("stderr");
//
//                new StreamCopier(shell.getInputStream(), shell.getOutputStream(), LoggerFactory.DEFAULT)
//                        .bufSize(shell.getRemoteMaxPacketSize())
//                        .copy();

                // TODO: 这里的ssh连接需要有一个断开的机制，比如点击窗口上面的x，或者输出 exit ， 目前写死睡眠
                Thread.sleep(100000000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                session.close();
            }

        } finally {
            ssh.disconnect();
        }
    }


    public Writer getOutputWriter() {
        return outputWriterProperty.get();
    }
}
