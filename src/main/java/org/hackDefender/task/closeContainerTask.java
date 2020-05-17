package org.hackDefender.task;

import org.hackDefender.service.ContainerService;
import org.hackDefender.util.RedisPoolSharedUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author vvings
 * @version 2020/4/19 17:25
 */


@Component
public class closeContainerTask {
    @Autowired
    private ContainerService containerService;


    public void AutoCloseContainer() {
        if (RedisPoolSharedUtil.trylock("close_lock", 5)) {
            close("close_lock");
        }
    }

    private void close(String lockName) {
        RedisPoolSharedUtil.expire(lockName, 5);
        containerService.AutoCloseContainer();
        RedisPoolSharedUtil.del(lockName);

    }
}
