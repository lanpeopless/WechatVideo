package com.jonas;

import com.jonas.Application;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * @author cmj
 * @date 2019-05-02 14:26
 */
public class WarStartApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // 使用web.xml运行指向application，启动springboot
        return builder.sources(Application.class);
    }
}
