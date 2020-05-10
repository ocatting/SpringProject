package com.ts.parser;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public interface IDtsParser {

    /**
     * JSON 字符串转为对象
     *
     * @param jsonStr   JSON 字符串
     * @param valueType 转换对象类
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> T readValue(String jsonStr, Class<T> valueType) throws Exception;

    /**
     * 对象转换为 JSON 字符串
     *
     * @param object 转换对象
     * @return
     * @throws Exception
     */
    String toJSONString(Object object) throws Exception;

    String objToString(Object object) throws Exception;
}
