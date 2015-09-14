package org.fiware.kiara;

import org.fiware.kiara.impl.ContextImpl;

import java.util.ArrayList;
import java.util.List;

import org.fiware.kiara.dynamic.DynamicValueBuilder;
import org.fiware.kiara.dynamic.impl.DynamicValueBuilderImpl;
import org.fiware.kiara.typecode.TypeDescriptorBuilder;
import org.fiware.kiara.typecode.TypeDescriptorBuilderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is the main entry point to use Advanced Middleware middleware.
 * It creates or provides implementation of top level Advanced Middleware interfaces,
 * especially {@link Context}.
 */
public class Kiara {

    private static final Logger logger = LoggerFactory.getLogger(Kiara.class);
    private static final List<RunningService> runningServices = new ArrayList<>();

    /**
     * This function returns an instance of the type {@link TypeDescriptorBuilder}.
     * @return Returns instance of the type {@link TypeDescriptorBuilder}.
     */
    public static TypeDescriptorBuilder getTypeDescriptorBuilder() {
        return TypeDescriptorBuilderImpl.getInstance();
    }

    /**
     * This function returns an instance of the type {@link DynamicValueBuilder}.
     * @return Returns instance of the type {@link DynamicValueBuilder}.
     */
    public static DynamicValueBuilder getDynamicValueBuilder() {
        return DynamicValueBuilderImpl.getInstance();
    }

    /**
     * This function creates a new instance of the Context class, which is part of the public Advanced Middleware RPC API .
     * @return Returns new Context instance.
     */
    public static Context createContext() {
        return new ContextImpl();
    }

    /**
     * Add service that will be shutdown when {@link #shutdown} is called.
     * @param service
     */
    public static void addRunningService(RunningService service) {
        synchronized (runningServices) {
            runningServices.add(service);
        }
    }

    /**
     * Remove service that was added with {@link #addRunningService}.
     * @param service
     */
    public static void removeRunningService(RunningService service) {
        synchronized (runningServices) {
            runningServices.remove(service);
        }
    }

    /**
     * This function closes and releases all internal Advanced Middleware structures.
     * (e.g. stops all pending tasks). Call this before you exit from your application.
     */
    public static void shutdown() {
        RunningService[] tmp;
        synchronized (runningServices) {
            tmp = runningServices.toArray(new RunningService[runningServices.size()]);
        }
        logger.info("shutdown {} services", tmp.length);
        for (RunningService s : tmp) {
            logger.info("shutdown {}", s);
            s.shutdownService();
        }
    }
}
