package com.alibaba.csp.sentinel.adapter.dubbo.fallback.springbootautoconfigure;

import com.alibaba.csp.sentinel.adapter.dubbo.config.DubboAdapterGlobalConfig;
import com.alibaba.csp.sentinel.adapter.dubbo.fallback.core.DubboFallbackFactory;
import com.alibaba.csp.sentinel.adapter.dubbo.fallback.core.SentinelDubboFallback;
import com.alibaba.csp.sentinel.adapter.dubbo.fallback.core.SentinelDubboFallbackContext;
import com.alibaba.csp.sentinel.adapter.dubbo.fallback.core.SentinelDubboFallbackFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * The bean type of {@link DubboFallbackFactory} which is indicated by {@link SentinelDubboFallbackFactory} will register into {@link DubboAdapterGlobalConfig}.
 *
 * @author Xiaowei Wen
 * @see DubboFallbackFactory
 * @see SentinelDubboFallbackFactory
 * @see DubboAdapterGlobalConfig
 * @since 1.8.3
 */
@Component
public class DubboFallbackApplicationContextAware implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        final Map<String, DubboFallbackFactory> beans = applicationContext.getBeansOfType(DubboFallbackFactory.class);
        final SentinelDubboFallbackContext fallbackContext = applicationContext.getBean(SentinelDubboFallbackContext.class);
        final ConcurrentMap<Class<?>, DubboFallbackFactory> factoryDefinition = fallbackContext.getFactoryDefinition();
        final SentinelDubboFallback sentinelDubboFallback = applicationContext.getBean(SentinelDubboFallback.class);

        initFactoryDefinition(factoryDefinition, beans);

        DubboAdapterGlobalConfig.setConsumerFallback(sentinelDubboFallback);
    }

    private void initFactoryDefinition(ConcurrentMap<Class<?>, DubboFallbackFactory> factoryDefinition, Map<String, DubboFallbackFactory> beans) {
        if (!ObjectUtils.isEmpty(beans)) {
            beans.forEach((k, v) -> {
                final Class<? extends DubboFallbackFactory> aClass = v.getClass();
                if (aClass.isAnnotationPresent(SentinelDubboFallbackFactory.class)) {
                    final SentinelDubboFallbackFactory annotation = aClass.getAnnotation(SentinelDubboFallbackFactory.class);
                    final Class<?> dubboInterface = annotation.dubboInterface();
                    factoryDefinition.put(dubboInterface, v);
                }
            });
        }
    }
}
