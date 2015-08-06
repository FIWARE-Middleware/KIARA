package org.fiware.kiara.ps.rtps.utils;

public class SystemUtils {

    public static long getPID() {
        String processName =
                java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
        return Long.parseLong(processName.split("@")[0]);
    }

}
