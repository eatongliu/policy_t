package com.gpdata.wanyou.base.listener;

import com.gpdata.wanyou.utils.ConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;


/**
 * 在框架启动前初始化一些环节变量用途.
 *
 * @author chengchao
 */
public class SystemPropertyListener implements ServletContextListener {

    private static final
    Logger LOGGER = LoggerFactory.getLogger(SystemPropertyListener.class);


    @Override
    public void contextInitialized(ServletContextEvent sce) {

        ServletContext servletContext = sce.getServletContext();

        String contextPath = servletContext.getContextPath();

        if (contextPath.equals("/")) {
            contextPath = "";
        }
        //servletContext.setAttribute("staticResPrefix", contextPath + "/static-res/");
        //servletContext.setAttribute("basePath", contextPath + "/");

        String wanyouProjectRoot = servletContext.getRealPath("/");
        //System.setProperty("wanyou.project.root", wanyouProjectRoot);

        LOGGER.debug("添加配置: wanyou.project.root = {}", wanyouProjectRoot);
        ConfigUtil.setConfig("wanyou.project.root", wanyouProjectRoot);

        if (LOGGER.isDebugEnabled()) {
            new Thread(() -> {
                while (true) {
                    Runtime run = Runtime.getRuntime();
                    long max = run.maxMemory();
                    long total = run.totalMemory();
                    long free = run.freeMemory();
                    long usable = max - total + free;
                    LOGGER.debug(">>>>");
                    LOGGER.debug("{}", LocalDateTime.now());
                    LOGGER.debug("最大内存数 m = {}" , sizeToString(max));
                    LOGGER.debug("已分配内存 t = {}" , sizeToString(total));
                    LOGGER.debug("剩余空间数 f = {}" , sizeToString(free));
                    LOGGER.debug("最大可用数 (m-t+f) = {}" , sizeToString(usable));
                    LOGGER.debug("<<<<");
                    try {
                        TimeUnit.SECONDS.sleep(300L);
                    } catch (InterruptedException e) {
                        LOGGER.debug(e.getMessage());
                        break;
                    }
                }
            }).start();
        }


        LOGGER.debug("SystemPropertyListener contextInitialized 执行完成 ...");

    }

    private static String sizeToString(long size) {
        double d = (double) size / 1024 / 1024 ;
        return String.format("% 10.10f 兆", d);

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

}
