package org.jfinger.cloud.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;

/**
 * @Description fastjson序列化
 * @Author finger
 * @Date 2021/1/5 0005
 * @Version 1.0
 */
public class FastJson2JsonRedisSerializer<T> implements RedisSerializer<T> {
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    static {
        ParserConfig.getGlobalInstance().addAccept("org.jfinger.cloud.entity.data");
        ParserConfig.getGlobalInstance().addAccept("org.jfinger.cloud.entity.model");
        ParserConfig.getGlobalInstance().addAccept("org.jfinger.cloud.entity.vo");
    }

    private Class<T> clazz;

    public FastJson2JsonRedisSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    /**
     * 序列化
     *
     * @param t
     * @return
     * @throws SerializationException
     */
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }
        return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(DEFAULT_CHARSET);
    }

    /**
     * 反序列化
     *
     * @param bytes
     * @return
     * @throws SerializationException
     */
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        String str = new String(bytes, DEFAULT_CHARSET);

        return (T) JSON.parseObject(str, clazz);
    }
}
