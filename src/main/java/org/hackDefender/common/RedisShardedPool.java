package org.hackDefender.common;

import org.hackDefender.util.PropertiesUtil;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vvings
 * @version 2020/4/20 21:45
 */
public class RedisShardedPool {
    private static ShardedJedisPool pool;//jedis连接池
    private static String ip = PropertiesUtil.getProperty("redis.ip1");
    private static String password = PropertiesUtil.getProperty("redis.password");
    private static Integer port = Integer.parseInt(PropertiesUtil.getProperty("redis.port1"));

    private static String ip2 = PropertiesUtil.getProperty("redis.ip2");
    private static Integer port2 = Integer.parseInt(PropertiesUtil.getProperty("redis.port2"));
    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.total"));//最大连接处
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle"));//最大的空闲实例个数
    private static Integer minIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle")); //最小的空闲实例个数
    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow"));//在borrow一个实例的，是否要进行验证操作，如果是true，则得到的jedis一定是可以用的
    private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return")); //在还一个实例的，是否要进行验证操作，如果是true，则得到的jedis一定是可以用的

    private static void initPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setMaxTotal(maxTotal);
        config.setTestOnReturn(testOnReturn);
        config.setTestOnBorrow(testOnBorrow);
        config.setBlockWhenExhausted(true);
        JedisShardInfo info1 = new JedisShardInfo(ip, port, 4000 * 2);
        info1.setPassword(password);
        System.out.println(info1.getHost());
        JedisShardInfo info2 = new JedisShardInfo(ip2, port2, 4000 * 2);
        info2.setPassword(password);
        List<JedisShardInfo> jedisShardInfoList = new ArrayList<JedisShardInfo>(2);
        jedisShardInfoList.add(info1);
        jedisShardInfoList.add(info2);
        pool = new ShardedJedisPool(config, jedisShardInfoList, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);
    }

    static {
        initPool();
        
    }

    public static ShardedJedis getShardedJedis() {
        return pool.getResource();
    }

    public static void returnBrokenResource(ShardedJedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    public static void returnResource(ShardedJedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    public static void main(String[] args) {
        ShardedJedis jedis = pool.getResource();
        for (int i = 0; i < 10; i++) {
            jedis.set("key" + i, "value" + i);
        }
        returnResource(jedis);

    }
}
