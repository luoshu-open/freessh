package org.freessh.sshclient.component.tab;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.freessh.sshclient.util.CollectionUtil;
import org.freessh.sshclient.util.WindowUtil;

import java.util.List;

/**
 * 自定义选项卡
 * @author 朱小杰
 */
public class TabView extends VBox {

    private ObservableList<Tab> tabs = FXCollections.observableArrayList();

    public static final Color headerColor = Color.web("#DFDFDF");
    private Color tagActiveColor = Color.web("#FFFFFF");

    /**
     *  tab 列表
     */
    private HBox hBox;
    private Pane bodyPane;

    /**
     * 选项卡的高度
     */
    public static final int headerHeight = 40;

    /**
     * 可以注册 tab 被选中的事件
     */
    private TabSelectListener tabSelectListener;

    /**
     * 当前选中的 tab
     */
    private Tab currentTab;


    public TabView() {
        tabs.addListener(new TabChangeListener());

        // 上面的是选项卡
        this.hBox = new HBox();
        this.hBox.setSpacing(3);
        this.hBox.prefWidthProperty().bind(this.widthProperty());
        this.hBox.setPrefHeight(headerHeight);
        this.hBox.setBackground(WindowUtil.createBackground(headerColor));
        this.hBox.setBackground(WindowUtil.createBackground(Color.web("#E1E1E1")));
        this.hBox.setStyle("-fx-border-width: 0 0 1 0; -fx-border-color: #ccc;");
        this.getChildren().add(this.hBox);

        // 下面的是选项卡的内容
        this.bodyPane = new Pane();
        this.bodyPane.prefWidthProperty().bind(this.widthProperty());
        this.prefHeightProperty().addListener((observable , old , newValue) -> {
            this.bodyPane.setPrefHeight(newValue.doubleValue() - headerHeight);
        });
        this.bodyPane.setBackground(WindowUtil.createBackground(Color.BLACK));
        this.getChildren().add(this.bodyPane);

    }

    public ObservableList<Tab> getTabs() {
        return tabs;
    }

    /**
     * 当添加了一个 tab 时触发 ， 只用于内部调用
     * @param tab
     */
    protected void addTab(Tab tab){


        this.hBox.getChildren().add(tab.getHeader());

        Region body = (Region) tab.getBody();
        body.prefWidthProperty().bind(this.bodyPane.widthProperty());
        body.prefHeightProperty().bind(this.bodyPane.heightProperty());

        // 注册关闭 tab 的事件
        tab.setCloseEvent(e -> {
            this.getTabs().remove(tab);
        });

        this.setBody(body);

        // 选中这个 tab
        this.selectTab(tab);

        // 注册 tab 的事件
        this.registerTabEvent(tab);
    }

    /**
     * 删除一个 tab ， 内部调用
     * @param tab
     */
    protected void removeTab(Tab tab){
        if(!this.currentTab.equals(tab)){
            this.getTabs().remove(tab);
            this.hBox.getChildren().remove(tab.getHeader());
        }
    }


    private void setBody(Node pane){
        this.bodyPane.getChildren().clear();
        this.bodyPane.getChildren().add(pane);
    }


    /**
     * 注册 tab 的点击事件
     * @param r
     */
    protected void registerTabEvent(Tab r){
        r.onClick(e -> {
            this.selectTab(r);
        });
    }

    /**
     * 选中一个 tab
     * @param tab
     */
    public void selectTab(Tab tab){
        // 把之前的 tab 取消激活
        if (this.getSelectTab() != null) {
            // 如果和当前的不是同一个，就取消
            if (!this.getSelectTab().equals(tab)) {
                this.getSelectTab().getHeader().cancelSelect();
            }
        }

        // 把当前添加的 tab 设置为激活
        if(this.getSelectTab() == null){
            this.currentTab = tab;
            tab.getHeader().select();
        }
        if(this.getSelectTab() != null &&
                !this.getSelectTab().equals(tab)){
            this.currentTab = tab;
            tab.getHeader().select();
        }

        this.setBody(tab.getBody());

        if(this.tabSelectListener != null){
            this.tabSelectListener.handler(tab);
        }

    }

    /**
     * 注册一个 tab 被改变的监听器
     * @param listener
     */
    public void setTabSelectListener(TabSelectListener listener){
        this.tabSelectListener = listener;
    }

    /**
     * 获取当前选中的 tab
     * @return
     */
    public Tab getSelectTab(){
        return this.currentTab;
    }



    private class TabChangeListener implements ListChangeListener<Tab>{

        @Override
        public void onChanged(Change<? extends Tab> c) {
            while (c.next()){
                if(c.wasAdded()){
                    // 新增一个
                    List<? extends Tab> addedSubList = c.getAddedSubList();
                    if(!CollectionUtil.isEmpty(addedSubList)){
                        for (Tab tab : addedSubList) {
                            addTab(tab);
                        }
                    }
                }
                // 删除一个
                else if(c.wasRemoved()){
                    List<? extends Tab> removed = c.getRemoved();
                    if(!CollectionUtil.isEmpty(removed)){
                        for (Tab tab : removed) {
                            removeTab(tab);
                        }
                    }
                }
            }
        }
    }
}
