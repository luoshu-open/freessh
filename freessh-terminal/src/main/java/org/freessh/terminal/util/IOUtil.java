package org.freessh.terminal.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author 朱小杰
 */
public class IOUtil {

    public static String toString(InputStream in) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(in , StandardCharsets.UTF_8));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null){
            sb.append(line);
            sb.append("\n");
        }
        if("".equals(sb.toString())){
            return "";
        }
        return sb.substring(0 , sb.length() - 1);
    }
}
