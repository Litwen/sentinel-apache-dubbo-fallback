package com.alibaba.csp.sentinel.adapter.dubbo.fallback.core;

import java.util.concurrent.ConcurrentMap;

/**
 * Context of the fallback class {@link SentinelDubboFallback}.
 * This class may hold some important information.
 *
 * @author Xiaowei Wen
 * @see SentinelDubboFallback
 * @since 1.8.3
 */
public class SentinelDubboFallbackContext {

    private final ConcurrentMap<Class<?>, DubboFallbackFactory> factoryDefinition;

    public SentinelDubboFallbackContext(ConcurrentMap<Class<?>, DubboFallbackFactory> factoryDefinition) {
        this.factoryDefinition = factoryDefinition;
    }

    public ConcurrentMap<Class<?>, DubboFallbackFactory> getFactoryDefinition() {
        return factoryDefinition;
    }
}
