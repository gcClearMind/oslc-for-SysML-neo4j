package com.example.oslc.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//@Component
public class MyServletContextListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(MyServletContextListener.class);
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 初始化代码
        System.out.println("Context Initialized 114514");
        logger.info("Context Initialized 114514");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // 清理代码
        System.out.println("Context Destroyed");
        logger.info("Context Destroyed");
    }
}