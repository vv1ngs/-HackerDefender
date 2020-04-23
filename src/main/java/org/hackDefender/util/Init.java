package org.hackDefender.util;

import org.apache.commons.lang.time.DateUtils;
import org.glassfish.jersey.internal.guava.Sets;
import org.hackDefender.common.RedisShardedPool;
import org.hackDefender.dao.ContainerMapper;
import org.hackDefender.pojo.Container;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.ShardedJedis;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author vvings
 * @version 2020/4/21 17:33
 */
@Component
public class Init {
    @Autowired
    private ContainerMapper containerMapper;

    @PostConstruct
    public void redisInit() {
        Date closeDate = DateUtils.addSeconds(new Date(), -Integer.parseInt(PropertiesUtil.getProperty("container_lasttime", "3600")));
        List<Container> containerList = containerMapper.selectByTime(DateTimeUtil.DateToString(closeDate));
        Set<Integer> set = Sets.newHashSet();
        for (Container container : containerList) {
            if (container.getPort() != 0) {
                set.add(container.getPort());
            }
        }
        ShardedJedis jedis = RedisShardedPool.getShardedJedis();
        Integer minport = Integer.parseInt(PropertiesUtil.getProperty("redis.min.port"));
        Integer maxport = Integer.parseInt(PropertiesUtil.getProperty("redis.max.port"));
        for (int i = minport; i <= maxport; i++) {
            if (!set.contains(i)) {
                jedis.sadd("hackDefender_port", String.valueOf(i));
            }
        }
    }
}
