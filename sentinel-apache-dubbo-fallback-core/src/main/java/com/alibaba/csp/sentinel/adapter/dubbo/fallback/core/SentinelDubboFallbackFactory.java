package com.alibaba.csp.sentinel.adapter.dubbo.fallback.core;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Indicates that an annotated class is a "FallbackFactory".This class must implement this {@link DubboFallbackFactory} interface.
 *
 * @author Xiaowei Wen
 * @see DubboFallbackFactory
 * @since 1.8.3
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Inherited
public @interface SentinelDubboFallbackFactory {

    /**
     * @return The specified dubbo interface which need fallback.
     */
    Class<?> dubboInterface();
}
