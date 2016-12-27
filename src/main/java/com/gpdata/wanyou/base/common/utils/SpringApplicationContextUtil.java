/*
 *
 */

package com.gpdata.wanyou.base.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * ApplicationContextAware 的一个实现 <br />
 * 应该将其放到 spring 配置文件中<br />
 * 用法 : <br />
 * <pre>
 * &lt;bean class="com.gpdata.trade.utils.SpringApplicationContextUtil" factory-method="getInstance" /&gt;
 * </pre>
 *
 * @author 程超
 * @version 1.0
 * @created 2015-2-11 下午2:30:36
 */
public class SpringApplicationContextUtil implements ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringApplicationContextUtil.class);
    /**
     * 单例
     */
    private static final
    SpringApplicationContextUtil INSTANCE = new SpringApplicationContextUtil();
    /**
     * {@code ApplicationContext }
     */
    private ApplicationContext applicationContext;

    /**
     * 构造函数
     */
    private SpringApplicationContextUtil() {
        LOGGER.debug("construct -> SpringApplicationContextUtil");
        // nothing ...
    }

    /**
     * <p >
     * 获取单例实例  <br />
     * </p>
     *
     * @return {@code AppCtxUtil }
     */
    public static final SpringApplicationContextUtil getInstance() {
        return INSTANCE;
    }

    /**
     * <p >
     * 通过名称(id) 获取被 Spring 托管的 Bean 实例.  <br />
     * </p>
     *
     * @param name bean 的名称 (id)
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T getByName(String name) {
        return (T) applicationContext.getBean(name);
    }

    /**
     * <p >
     * 通过类型获取被 Spring 托管的 Bean 实例.  <br />
     * </p>
     *
     * @param type
     * @return
     */
    public <T> T getByType(Class<T> requiredType) {
        return this.applicationContext.getBean(requiredType);
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * ApplicationContextAware
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;
    }


}
