package com.wre.game.api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.util.Collection;

//@Configuration
//@EnableCaching
public class EhCachingConfig {

    private static final Logger logger = LoggerFactory.getLogger(EhCachingConfig.class);

    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {

        StringBuilder pathBuilder = new StringBuilder("config/cache/");
        pathBuilder.append("ehcache.xml");

        logger.info("Ehcache Config Location : " + pathBuilder.toString());
        EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean();

        cacheManagerFactoryBean.setConfigLocation(new ClassPathResource(pathBuilder.toString()));
        cacheManagerFactoryBean.setShared(true);

        return cacheManagerFactoryBean;
    }

    @Bean(name = "cacheManager")
    public EhCacheCacheManager ehCacheCacheManager() {
        EhCacheCacheManager manager = new EhCacheCacheManager(ehCacheManagerFactoryBean().getObject());
        Collection<String> cacheNames = manager.getCacheNames();
        for (String cacheName : cacheNames) {
            logger.info("Ehcache Config CacheName=" + cacheName);
        }
        return manager;
    }
}
