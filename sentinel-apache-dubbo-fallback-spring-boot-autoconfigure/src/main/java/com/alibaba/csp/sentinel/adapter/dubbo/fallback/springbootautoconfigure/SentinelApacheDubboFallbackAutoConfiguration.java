package com.alibaba.csp.sentinel.adapter.dubbo.fallback.springbootautoconfigure;

import com.alibaba.csp.sentinel.adapter.dubbo.fallback.DubboFallback;
import com.alibaba.csp.sentinel.adapter.dubbo.fallback.core.DubboFallbackFactory;
import com.alibaba.csp.sentinel.adapter.dubbo.fallback.core.SentinelDubboFallback;
import com.alibaba.csp.sentinel.adapter.dubbo.fallback.core.SentinelDubboFallbackContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Xiaowei Wen
 * @since 1.8.3
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(DubboFallback.class)
public class SentinelApacheDubboFallbackAutoConfiguration {

    @Bean
    public SentinelDubboFallbackContext sentinelDubboFallbackContext() {
        final ConcurrentHashMap<Class<?>, DubboFallbackFactory> factoryDefinition = new ConcurrentHashMap<>(8);
        return new SentinelDubboFallbackContext(factoryDefinition);
    }

    @Bean
    public SentinelDubboFallback sentinelDubboFallback(SentinelDubboFallbackContext sentinelDubboFallbackContext) {
        return new SentinelDubboFallback(sentinelDubboFallbackContext);
    }
}
