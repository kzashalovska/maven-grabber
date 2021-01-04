package com.smartfoxpro.mavengrabber.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class AsyncConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncConfiguration.class);
    @Value("${app.pools.maxPoolSize}")
    private int maxPoolSize;
    @Value("${app.pools.corePoolSize}")
    private int corePoolSize;

    @Bean(name = "taskExecutor")
    public ThreadPoolExecutor taskExecutor() {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaximumPoolSize(maxPoolSize);

        return executor;
    }
}
