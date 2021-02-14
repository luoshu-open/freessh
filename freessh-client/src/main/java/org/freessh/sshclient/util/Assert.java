package org.freessh.sshclient.util;

import java.util.Collection;

/**
 * @author 朱小杰
 */
public class Assert {

    public static void assertNotBlank(String text , String errMsg){
        if(StringUtil.isBlank(text)){
            throw new NullPointerException(errMsg);
        }
    }

    public static void assertNotEmpty(Collection<?> collection , String errMsg){
        if(collection == null || collection.size() == 0){
            throw new NullPointerException(errMsg);
        }
    }

    public static void assertNotNull(Object obj , String errMsg){
        if(obj == null){
            throw new NullPointerException(errMsg);
        }
    }
}
