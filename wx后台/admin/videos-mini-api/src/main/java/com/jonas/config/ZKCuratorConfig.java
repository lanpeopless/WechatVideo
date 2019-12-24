package com.jonas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author cmj
 * @date 2019-04-14 16:43
 */
@Configuration
public class ZKCuratorConfig {

    @Bean(initMethod = "init")
    public ZKCuratorClient zkCuratorClient() {
        ZKCuratorClient zkClient = new ZKCuratorClient();
        return zkClient;
    }

}
