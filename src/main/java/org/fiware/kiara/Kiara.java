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

public class Kiara {

    private static final Logger logger = LoggerFactory.getLogger(Kiara.class);
    private static final List<RunningService> runningServices = new ArrayList<>();

    public static TypeDescriptorBuilder getTypeDescriptorBuilder() {
        return TypeDescriptorBuilderImpl.getInstance();
    }

    public static DynamicValueBuilder getDynamicValueBuilder() {
        return DynamicValueBuilderImpl.getInstance();
    }

    public static Context createContext() {
        return new ContextImpl();
    }

    public static void addRunningService(RunningService service) {
        synchronized (runningServices) {
            runningServices.add(service);
        }
    }

    public static void removeRunningService(RunningService service) {
        synchronized (runningServices) {
            runningServices.remove(service);
        }
    }

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
