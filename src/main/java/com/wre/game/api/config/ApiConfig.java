package com.wre.game.api.config;

import com.wre.game.api.handler.HttpLogInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class ApiConfig implements WebMvcConfigurer {

    //交给spring管理，不然无法依赖别的ben
    @Bean
    HttpLogInterceptor httpLogInterceptor(){
        return new HttpLogInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(httpLogInterceptor());
    }
}
