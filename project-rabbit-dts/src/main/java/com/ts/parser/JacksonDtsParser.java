package com.ts.parser;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public class JacksonDtsParser implements IDtsParser {

    private static ObjectMapper objectMapper;

    public static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
        return objectMapper;
    }

    @Override
    public <T> T readValue(String jsonStr, Class<T> valueType) throws Exception {
        if (null == jsonStr || "".equals(jsonStr)) {
            return null;
        }
        return getObjectMapper().readValue(jsonStr, valueType);
    }

    @Override
    public String toJSONString(Object object) throws Exception {
        return getObjectMapper().writeValueAsString(object);
    }

    @Override
    public String objToString(Object object) throws Exception {
        if(object instanceof String){
            return (String) object;
        }
        return toJSONString(object);
    }
}
