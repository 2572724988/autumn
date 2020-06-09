package com.autumn.util;

import java.lang.reflect.Field;

import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;

/**
 * Aop目标工具
 * 
 * @author 老码农 2019-05-04 18:56:13
 */
public class AopTargetUtils {

	/**
	 * 获取 目标对象
	 * 
	 * @param proxy
	 *            代理对象
	 * @return 目标对象
	 * @throws Exception
	 */
	public static Object getTarget(Object proxy) throws Exception {
		if (!AopUtils.isAopProxy(proxy)) {
			return proxy;
		}
		if (AopUtils.isJdkDynamicProxy(proxy)) {
			proxy = getJdkDynamicProxyTargetObject(proxy);
		} else {
			proxy = getCglibProxyTargetObject(proxy);
		}
		return getTarget(proxy);
	}

	private static Object getCglibProxyTargetObject(Object proxy) throws Exception {
		Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
		h.setAccessible(true);
		Object dynamicAdvisedInterceptor = h.get(proxy);
		Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
		advised.setAccessible(true);
		Object target = ((AdvisedSupport) advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
		return target;
	}

	private static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
		Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
		h.setAccessible(true);
		AopProxy aopProxy = (AopProxy) h.get(proxy);
		Field advised = aopProxy.getClass().getDeclaredField("advised");
		advised.setAccessible(true);
		Object target = ((AdvisedSupport) advised.get(aopProxy)).getTargetSource().getTarget();
		return target;
	}
}