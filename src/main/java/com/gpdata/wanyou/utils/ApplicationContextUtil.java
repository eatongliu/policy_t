package com.gpdata.wanyou.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * ApplicationContextAware 实现 <br />
 * 应该将其放到 spring 配置文件中<br />
 * <p>
 * &lt;bean class="com.gpdata.wanyou.utils.ApplicationContextUtil" factory-method="getInstance" /&gt;
 * <p>
 * Created by chengchao on 16-10-11.
 */
public class ApplicationContextUtil implements ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationContextUtil.class);
    /**
     * 单例
     */
    private static final ApplicationContextUtil INSTANCE = new ApplicationContextUtil();
    /**
     * {@code ApplicationContext }
     */
    private ApplicationContext applicationContext;


    /**
     * 构造函数
     */
    private ApplicationContextUtil() {
        // nothing ...
        //LOGGER.info("构造工具类 : ApplicationContextUtil ");
    }

    /**
     * <p style="color: #f20;">
     * 获取单例实例  <br />
     * </p>
     *
     * @return {@code ApplicationContextUtil }
     */
    public static final ApplicationContextUtil getInstance() {
        return INSTANCE;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * Set the ApplicationContext that this object runs in.
     * Normally this call will be used to initialize the object.
     * <p>Invoked after population of normal bean properties but before an init callback such
     * as {@link InitializingBean#afterPropertiesSet()}
     * or a custom init-method. Invoked after {@link ResourceLoaderAware#setResourceLoader},
     * {@link ApplicationEventPublisherAware#setApplicationEventPublisher} and
     * {@link MessageSourceAware}, if applicable.
     *
     * @param applicationContext the ApplicationContext object to be used by this object
     * @throws ApplicationContextException in case of context initialization errors
     * @throws BeansException              if thrown by application context methods
     * @see BeanInitializationException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        this.applicationContext = applicationContext;
    }

    /**
     * <p style="color: #f20;">
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
     * <p style="color: #f20;">
     * 通过类型获取被 Spring 托管的 Bean 实例.  <br />
     * </p>
     *
     * @param requiredType
     * @return
     */
    public <T> T getByType(Class<T> requiredType) {

        return this.applicationContext.getBean(requiredType);

    }


}
