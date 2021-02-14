package org.freessh.sshclient.component.tab;

/**
 * tab 被选中的监听器
 *
 * @author 朱小杰
 */
public interface TabSelectListener {


    /**
     * 代表着这个 tab 被选中
     * @param tab
     */
    void handler(Tab tab);

}
