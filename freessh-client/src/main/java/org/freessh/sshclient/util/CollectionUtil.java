package org.freessh.sshclient.util;

import java.util.Collection;

/**
 * @author 朱小杰
 */
public class CollectionUtil {

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.size() == 0;
    }
}
