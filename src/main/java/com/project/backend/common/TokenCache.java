package com.project.backend.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author Utopia
 */
public class TokenCache {

    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);

    //基于调用链创建本地缓存
    /*
     * <key, value> - <String, String>
     * 初始化容量为1000， 最大容量为10000，有效期为12h
     */
    private static LoadingCache<String, String> localCache = CacheBuilder.newBuilder().initialCapacity(1000).
            maximumSize(10000).expireAfterAccess(12, TimeUnit.HOURS).build(new CacheLoader<String, String>() {
        //默认的数据加载实现，get取值时如果没有对应的key则调用此方法
        @Override
        public String load(String s) throws Exception {
            return "null";
        }
    });

    public static void set(String key, String value) {
        localCache.put(key, value);
    }

    public static String getValue(String key) {
        try {
            String value = localCache.get(key);
            if ("null".equals(value)) {
                return null;
            }
            return value;
        } catch (ExecutionException e) {
            logger.error("local cache error", e);
            e.printStackTrace();
        }
        return null;
    }
}
