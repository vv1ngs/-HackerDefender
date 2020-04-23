package org.hackDefender.util;

import lombok.extern.slf4j.Slf4j;
import org.hackDefender.common.RedisShardedPool;
import redis.clients.jedis.ShardedJedis;

/**
 * @author vvings
 * @version 2020/4/20 21:55
 */
@Slf4j
public class RedisPoolSharedUtil {

    public static String set(String key, String value) {
        ShardedJedis jedis = null;
        String result = null;
        try {
            jedis = RedisShardedPool.getShardedJedis();
            result = jedis.set(key, value);
        } catch (Exception e) {
            log.error("set key:{} value:{} error:{}", key, value, e);
            RedisShardedPool.returnBrokenResource(jedis);
            return null;
        }
        return result;
    }

    public static String get(String key) {
        ShardedJedis jedis = null;
        String result = null;
        try {
            jedis = RedisShardedPool.getShardedJedis();
            result = jedis.get(key);
        } catch (Exception e) {
            log.error("set key:{} error:{}", key, e);
            RedisShardedPool.returnBrokenResource(jedis);
            return null;
        }
        return result;
    }

    public static String sPop() {
        ShardedJedis jedis = null;
        String result = null;
        try {
            jedis = RedisShardedPool.getShardedJedis();
            result = jedis.spop("hackDefender_port");
        } catch (Exception e) {
            log.error("error:{}", e);
            RedisShardedPool.returnBrokenResource(jedis);
            return null;
        }
        return result;
    }

    public static String getSet(String key, String value) {
        ShardedJedis jedis = null;
        String result = null;
        try {
            jedis = RedisShardedPool.getShardedJedis();
            result = jedis.getSet(key, value);
        } catch (Exception e) {
            log.error("set key:{} error:{}", key, e);
            RedisShardedPool.returnBrokenResource(jedis);
            return null;
        }
        return result;
    }

    public static Long expire(String key, int exTime) {
        ShardedJedis jedis = null;
        Long result = null;
        try {
            jedis = RedisShardedPool.getShardedJedis();
            result = jedis.expire(key, exTime);
        } catch (Exception e) {
            log.error("set key:{}  error:{}", key, e);
            RedisShardedPool.returnBrokenResource(jedis);
            return null;
        }
        return result;
    }

    public static Long expire(String key) {
        ShardedJedis jedis = null;
        Long result = null;
        try {
            jedis = RedisShardedPool.getShardedJedis();
            result = jedis.del(key);
        } catch (Exception e) {
            log.error("set key:{}  error:{}", key, e);
            RedisShardedPool.returnBrokenResource(jedis);
            return null;
        }
        return result;
    }

    public static Long setNx(String key, String value) {
        ShardedJedis jedis = null;
        Long result = null;
        try {
            jedis = RedisShardedPool.getShardedJedis();
            result = jedis.setnx(key, value);
        } catch (Exception e) {
            log.error("setEx key:{} value:{} error:{}", key, value, e);
            RedisShardedPool.returnBrokenResource(jedis);
            return null;
        }
        return result;
    }

    public static Long sAdd(Integer port) {
        ShardedJedis jedis = null;
        Long result = null;
        try {
            jedis = RedisShardedPool.getShardedJedis();
            result = jedis.sadd("hackDefender_port", String.valueOf(port));
        } catch (Exception e) {
            log.error("Add port:{} error:{}", port, e);
            RedisShardedPool.returnBrokenResource(jedis);
            return null;
        }
        return result;
    }

    public static String setEx(String key, String value, Integer time) {
        ShardedJedis jedis = null;
        String result = null;
        try {
            jedis = RedisShardedPool.getShardedJedis();
            result = jedis.setex(key, Integer.parseInt(value), String.valueOf(time));
        } catch (Exception e) {
            log.error("setEx key:{} value:{} error:{}", key, value, e);
            RedisShardedPool.returnBrokenResource(jedis);
            return null;
        }
        return result;
    }

    public static Long del(String key) {
        ShardedJedis jedis = null;
        Long result = null;
        try {
            jedis = RedisShardedPool.getShardedJedis();
            result = jedis.del(key);
        } catch (Exception e) {
            log.error("setEx key:{}  error:{}", key, e);
            RedisShardedPool.returnBrokenResource(jedis);
            return null;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }
}
