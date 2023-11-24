package com.ramon.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

    @Value("${redis.host:localhost}")
    private String redisHost;

    @Value("${redis.port:6379}")
    private int redisPort;

    @Bean
    public LettuceConnectionFactory letuceConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new LettuceConnectionFactory(configuration);
    }

//    @Bean
//    public RedisTemplate<String, Device> redisTemplate() {
//        RedisTemplate<String, Device> template = new RedisTemplate<>();
//        template.setConnectionFactory(letuceConnectionFactory());
//        template.setEnableTransactionSupport(true);
//        template.afterPropertiesSet();
//        return template;
//    }
}
