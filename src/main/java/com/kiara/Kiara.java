package com.kiara;

import com.kiara.impl.ContextImpl;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Kiara {

    private static final Logger logger = LoggerFactory.getLogger(Kiara.class);
    private static final List<RunningService> runningServices = new ArrayList<RunningService>();

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
