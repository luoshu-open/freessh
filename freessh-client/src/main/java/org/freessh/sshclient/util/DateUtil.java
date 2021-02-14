package org.freessh.sshclient.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 朱小杰
 */
public class DateUtil {



    public static String format(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}
