package code.pod.space.workspace.platform.workflow.util;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;

public class SelfUtil {
    private static String ip;

    static {
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            ip = "UNKNOWN";
        }
    }

    public static String getName() {

        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        String jvmPid = runtimeMXBean.getName().split("@")[0];
        return ip + "@" + jvmPid;
    }

    public static String getName(String prefix) {
        return prefix + "_" + getName();
    }
}
