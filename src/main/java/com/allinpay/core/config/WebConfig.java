package com.allinpay.core.config;

import com.allinpay.core.interceptor.AuthorityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@PropertySource("file:config/resource.properties")
public class WebConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private AuthorityInterceptor interceptor;
    @Value("${imgDir}")
    private String imgDir;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor).addPathPatterns("/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //访问js/css/img/file等静态资源 pathPatterns指的是url路径规则
        registry.addResourceHandler("/manage/static/**").addResourceLocations("classpath:/manage/static/");
        //配置磁盘外虚拟目录映射 用于访问上传的图片
        registry.addResourceHandler("/manage/etcimg/**").addResourceLocations("file:" + imgDir);
        //访问html资源，一般是设置进制访问的
        registry.addResourceHandler("/**").addResourceLocations("classpath:/templates/");
    }
}
