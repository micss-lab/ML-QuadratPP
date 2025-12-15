package org.thingml.xtext.web;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebListener;
import java.util.concurrent.ForkJoinPool;

@WebListener
public class CleanupListener implements ServletContextListener {

    @Override public void contextInitialized(ServletContextEvent sce) {}

    @Override public void contextDestroyed(ServletContextEvent sce) {
        /* 1) Stop EMF’s Reference‑Cleaner if EMF is present */
        try {
            Class<?> plugin = Class.forName("org.eclipse.emf.common.util.CommonPlugin");
            Object instance = plugin.getField("INSTANCE").get(null);
            plugin.getMethod("stop").invoke(instance);   // CommonPlugin.INSTANCE.stop()
        } catch (ClassNotFoundException e) {
            // EMF isn’t on the classpath – nothing to stop
        } catch (Throwable ignored) {}

        /* 2) Kill tasks in the global ForkJoinPool */
        ForkJoinPool.commonPool().shutdownNow();
    }
}


