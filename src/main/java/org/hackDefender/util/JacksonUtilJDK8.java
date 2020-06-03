package org.hackDefender.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Optional;

/**
 * @author vvings
 * @version 2020/6/1 22:34
 */
public class JacksonUtilJDK8 {
    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        //对象的所有字段全部json
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        //取消默认的timestamps形式
        objectMapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
        //忽略空bean转json的错误
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //所有的日期格式都统一为以下的样式，即yyyy-MM-dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        //忽略在json字符串中存在，在java对象中不存在的情况
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> String ObjToString(T obj) {

        return Optional.ofNullable(obj).map(b -> {
            try {
                return objectMapper.writeValueAsString(b);
            } catch (JsonProcessingException e) {
                return StringUtils.EMPTY;
            }
        }).orElse(StringUtils.EMPTY);
    }

    public static <T> String ObjToStringPretty(T obj) {

        return Optional.ofNullable(obj).map(b -> {
            try {
                return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(b);
            } catch (JsonProcessingException e) {
                return StringUtils.EMPTY;
            }
        }).orElse(StringUtils.EMPTY);
    }

    public static <T> T StringToObj(String str, Class<T> clazz) {
        return Optional.ofNullable(str).map(o -> {
            try {
                return objectMapper.readValue(o, clazz);
            } catch (IOException e) {
                return null;
            }
        }).orElse(null);
    }

    public static <T> T String2ToObj(String str, Class<?> collectionClass, Class<?>... elementClasses) {
        return (T) Optional.ofNullable(str).map(o -> {
            try {
                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
                return objectMapper.readValue(o, javaType);
            } catch (IOException e) {
                return null;
            }
        }).orElse(null);
        //String2ToObj(str,HashMap.class,String.class)
    }

    public static <T> T String2ToObj(String str, TypeReference<T> tTypeReference) {
        return (T) Optional.ofNullable(str).map(o -> {
            try {
                return objectMapper.readValue(str, tTypeReference);
            } catch (IOException ioException) {
                return null;
            }
        }).orElse(null);
    }
}
