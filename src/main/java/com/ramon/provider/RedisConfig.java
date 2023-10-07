package com.ramon.provider;

import com.ramon.provider.model.Device;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

    @Bean
    public LettuceConnectionFactory letuceConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration("localhost", 6379);
        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    public RedisTemplate<String, Device> redisTemplate() {
        RedisTemplate<String, Device> template = new RedisTemplate<>();
        template.setConnectionFactory(letuceConnectionFactory());
        template.setEnableTransactionSupport(true);
        template.afterPropertiesSet();
        return template;
    }
}
