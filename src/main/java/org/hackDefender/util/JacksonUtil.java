package org.hackDefender.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * @author vvings
 * @version 2020/3/27 10:27
 */
@Slf4j
public class JacksonUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        //对象的所有字段全部json
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        //取消默认的timestamps形式
        objectMapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
        //忽略空bean转json的错误
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //所有的日期格式都统一为以下的样式，即yyyy-MM-dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDER_FORMAT));
        //忽略在json字符串中存在，在java对象中不存在的情况
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> String ObjToString(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.warn("Parse object to  String error", e);
            return null;
        }
    }

    public static <T> String ObjToStringPretty(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.warn("Parse object to  String error", e);
            return null;
        }
    }


    public static <T> T StringToObj(String str, Class<T> clazz) {
        if (StringUtils.isEmpty(str) || clazz == null) {
            return null;
        }
        try {
            return clazz.equals(String.class) ? (T) str : objectMapper.readValue(str, clazz);
        } catch (IOException e) {
            log.warn("Parse String to  Object error", e);
            return null;
        }
    }

    public static <T> T String2ToObj(String str, TypeReference<T> tTypeReference) {
        if (StringUtils.isEmpty(str) || tTypeReference == null) {
            return null;
        }
        try {
            return (T) (tTypeReference.getType().equals(String.class) ? str : objectMapper.readValue(str, tTypeReference));
        } catch (IOException e) {
            log.warn("Parse String to  Object error", e);
            return null;
        }
    }

    public static <T> T String2ToObj(String str, Class<?> collectionClass, Class<?>... elementClasses) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);

        try {
            return objectMapper.readValue(str, javaType);
        } catch (IOException e) {
            log.warn("Parse String to  Object error", e);
            return null;
        }
    }

}
