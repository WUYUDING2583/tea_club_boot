package com.yuyi.tea.component;

import com.yuyi.tea.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 在项目启动时自动清空redis缓存
 */
@Component
public class CacheUpdateRunner implements CommandLineRunner {

    private final Logger log = LoggerFactory.getLogger(CacheUpdateRunner.class);

    @Autowired
    private RedisService redisService;

    @Override
    public void run(String... args) throws Exception {
        redisService.clearRedis();
        log.info("项目启动，清空redis缓存");
    }
}
