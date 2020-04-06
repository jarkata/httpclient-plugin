package com.jarkata.plugin.client.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JsonUtils {

    /**
     * JSON格式化
     *
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        return JSON.toJSONString(object, SerializerFeature.PrettyFormat,
                SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteEnumUsingName,
                SerializerFeature.WriteBigDecimalAsPlain);
    }
}
