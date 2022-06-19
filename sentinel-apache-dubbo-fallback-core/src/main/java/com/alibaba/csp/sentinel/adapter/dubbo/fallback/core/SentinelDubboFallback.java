package com.alibaba.csp.sentinel.adapter.dubbo.fallback.core;

import com.alibaba.csp.sentinel.adapter.dubbo.config.DubboAdapterGlobalConfig;
import com.alibaba.csp.sentinel.adapter.dubbo.fallback.DubboFallback;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.apache.dubbo.rpc.AsyncRpcResult;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;

/**
 * This class implement the interface {@link DubboFallback}.The then instance of this class will be registered.
 * Handle the block exception {@link BlockException} and provide fallback result.
 * <p>
 * Ex.
 * <pre>
 * {@code
 *        {
 *             final ConcurrentHashMap<Class<?>, DubboFallbackFactory> factoryDefinition = new ConcurrentHashMap<>(8);
 *             final SentinelDubboFallback dubboFallback = new SentinelDubboFallback(factoryDefinition);
 *             DubboAdapterGlobalConfig.setConsumerFallback(dubboFallback);
 *         }
 * }
 * </pre>
 *
 * @author Xiaowei Wen
 * @see DubboFallback
 * @see DubboAdapterGlobalConfig
 * @see BlockException
 * @since 1.8.3
 */
public class SentinelDubboFallback implements DubboFallback {

    private final SentinelDubboFallbackContext context;

    public SentinelDubboFallback(SentinelDubboFallbackContext context) {
        Assert.notNull(context.getFactoryDefinition(), "[SentinelDubboFallbackContext.factoryDefinition] must not be null");
        this.context = context;
    }

    /**
     * Handle the block exception and provide fallback result.
     */
    @Override
    public Result handle(Invoker<?> invoker, Invocation invocation, BlockException ex) {
        try {
            final String methodName = invocation.getMethodName();
            final Class<?> anInterface = invoker.getInterface();
            final Object[] arguments = invocation.getArguments();
            final Class<?>[] parameterTypes = invocation.getParameterTypes();
            final DubboFallbackFactory fallbackFactory = context.getFactoryDefinition().get(anInterface);
            final Object fallback = fallbackFactory.create(ex);

            if (!ObjectUtils.isEmpty(fallback)) {
                final Class<?> fallbackClass = fallback.getClass();
                final Method method = fallbackClass.getMethod(methodName, parameterTypes);
                final Object invoke = method.invoke(fallbackClass, arguments);
                return AsyncRpcResult.newDefaultAsyncResult(invoke, invocation);
            }

        } catch (Exception e) {
            // When exception but not blockException happened, just provide fallback result.
            return AsyncRpcResult.newDefaultAsyncResult(ex.toRuntimeException(), invocation);
        }

        return AsyncRpcResult.newDefaultAsyncResult(ex.toRuntimeException(), invocation);

    }


}
