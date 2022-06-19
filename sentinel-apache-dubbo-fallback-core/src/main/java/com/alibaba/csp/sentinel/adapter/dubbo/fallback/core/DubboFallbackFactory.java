package com.alibaba.csp.sentinel.adapter.dubbo.fallback.core;

/**
 * Used to control the fallback given its cause.
 * <p>
 * Ex.
 * <pre>
 *  {@code
 *  public interface OrderService {
 *     Object order(Object o);
 * }
 *
 * public class OrderServiceDubboFallbackFactory implements DubboFallbackFactory<OrderService> {
 *
 *     @Override
 *     public OrderService create(Throwable cause) {
 *         return new OrderService() {
 *             @Override
 *             public Object order(Object o) {
 *                 return null;
 *             }
 *         };
 *     }
 * }
 *  }
 * </pre>
 *
 * @param <T> The dubbo interface type
 * @author Xiaowei Wen
 * @since 1.8.3
 */
public interface DubboFallbackFactory<T> {

    /**
     * Returns an instance of the fallback appropriate for the given cause.
     *
     * @param cause cause of an exception
     * @return fallback
     */
    T create(Throwable cause);

}
