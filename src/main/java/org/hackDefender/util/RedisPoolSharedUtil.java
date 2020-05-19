package org.hackDefender.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hackDefender.common.Const;
import org.hackDefender.common.RedisShardedPool;
import redis.clients.jedis.ShardedJedis;

/**
 * @author vvings
 * @version 2020/4/20 21:55
 */
@Slf4j
public class RedisPoolSharedUtil {
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "EX";

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
        RedisShardedPool.returnResource(jedis);

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
        RedisShardedPool.returnResource(jedis);
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
        RedisShardedPool.returnResource(jedis);

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
        RedisShardedPool.returnResource(jedis);

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
        RedisShardedPool.returnResource(jedis);

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
        RedisShardedPool.returnResource(jedis);

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
        RedisShardedPool.returnResource(jedis);

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
        RedisShardedPool.returnResource(jedis);

        return result;
    }

    public static String setEx(String key, String value, int time) {
        ShardedJedis jedis = null;
        String result = null;
        try {
            jedis = RedisShardedPool.getShardedJedis();
            result = jedis.setex(key, time, value);
        } catch (Exception e) {
            log.error("setEx key:{} value:{} error:{}", key, value, e);
            RedisShardedPool.returnBrokenResource(jedis);
            return null;
        }
        RedisShardedPool.returnResource(jedis);

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

    public static boolean trylock(String key, int timeout) {
        Long endtime = timeout + System.currentTimeMillis();
        long lockTime = Long.parseLong(PropertiesUtil.getProperty("lock.time", "5000"));
        System.out.println(System.currentTimeMillis());
        while (System.currentTimeMillis() < endtime) {
            Long setResult = RedisPoolSharedUtil.setNx(key, String.valueOf(System.currentTimeMillis() + timeout));
            if (setResult != null && setResult.intValue() == 1) {
                return true;
            } else {
                String lockValueStr = RedisPoolSharedUtil.get(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
                if (lockValueStr != null && System.currentTimeMillis() > Long.parseLong(lockValueStr)) {
                    String getSetResult = RedisPoolSharedUtil.getSet(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis() + lockTime));
                    if (getSetResult == null || (getSetResult != null && StringUtils.equals(lockValueStr, getSetResult))) {
                        //真正获取到锁
                        return true;
                    } else {
                        log.info("没有获取到分布式锁:{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
                    }
                }
            }
            if (timeout == 0) {
                return false;
            }
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


}
