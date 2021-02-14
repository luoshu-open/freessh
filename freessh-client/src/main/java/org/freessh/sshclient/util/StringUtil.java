package org.freessh.sshclient.util;

/**
 * @author 朱小杰
 */
public class StringUtil {

    public static boolean isBlank(String text){
        return text == null || text.trim().equals("");
    }

}
