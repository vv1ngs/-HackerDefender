package org.hackDefender.task;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hackDefender.common.Const;
import org.hackDefender.service.ContainerService;
import org.hackDefender.util.PropertiesUtil;
import org.hackDefender.util.RedisPoolSharedUtil;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author vvings
 * @version 2020/4/19 17:25
 */

@Slf4j
public class closeContainerTask {
    @Autowired
    private ContainerService containerService;


    public void AutoCloseContainer() {
        long lockTime = Long.parseLong(PropertiesUtil.getProperty("lock.time", "5000"));
        Long setResult = RedisPoolSharedUtil.setNx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis() + lockTime));
        if (setResult != null && setResult.intValue() == 1) {
            close(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        } else {
            String lockValueStr = RedisPoolSharedUtil.get(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
            if (lockValueStr != null && System.currentTimeMillis() > Long.parseLong(lockValueStr)) {
                String getSetResult = RedisPoolSharedUtil.getSet(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis() + lockTime));
                if (getSetResult == null || (getSetResult != null && StringUtils.equals(lockValueStr, getSetResult))) {
                    //真正获取到锁
                    close(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
                } else {
                    log.info("没有获取到分布式锁:{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
                }
            }
        }
    }

    private void close(String lockName) {
        RedisPoolSharedUtil.expire(lockName, 5);
        log.info("获取{},ThreadName:{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, Thread.currentThread().getName());
        containerService.AutoCloseContainer();
        RedisPoolSharedUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
    }
}
