package com.jonas;

import com.jonas.controller.interceptor.MiniInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author cmj
 * @date 2019-03-10 19:17
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //映射文件路径
        //路径末尾的斜杠不能省略
        registry.addResourceHandler("/**")
        //上面的pathPatterns所对应的路径都会被处理成下面的两个资源路径
                .addResourceLocations("classpath:/META-INF/resources/")//防止swaqger页面加载不出来
                .addResourceLocations("file:/Video/UserFile/")
                .addResourceLocations("file:/Video/BGM/");
    }
    @Bean
    public MiniInterceptor miniInterceptor(){
        return new MiniInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 将Controller下的对应的请求都拦截
        registry.addInterceptor(miniInterceptor())
                .addPathPatterns("/user/uploadFaceImage")
                .addPathPatterns("/bgm/**")
                .addPathPatterns("/video/uploadVideo")
        ;
    }
}
