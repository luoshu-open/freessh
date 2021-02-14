package org.freessh.sshclient.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;

/**
 * json 工具
 *
 * @author 朱小杰
 */
public class Json {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    static {
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT , true);
    }

    public static String toJsonString(Object obj) {
        String s = null;
        try {
            s = objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage() , e);
        }
        return s;
    }

    public static <T> T parse(String text , Class<T> t){
        T o = null;
        try {
            o = objectMapper.readValue(text, t);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage() , e);
        }
        return o;
    }
}
