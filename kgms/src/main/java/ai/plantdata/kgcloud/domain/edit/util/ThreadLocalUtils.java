package ai.plantdata.kgcloud.domain.edit.util;

import org.springframework.core.NamedThreadLocal;

/**
 * @Author: LinHo
 * @Date: 2020/1/11 17:00
 * @Description:
 */
public class ThreadLocalUtils {

    private ThreadLocalUtils() {
    }

    public static final ThreadLocal<String> batchNo = new NamedThreadLocal<>("log batch number");

    public static String getBatchNo() {
        return batchNo.get();
    }

    public static void setBatchNo() {
        batchNo.set(getNextTimestamp());
    }

    public static void remove() {
        batchNo.remove();
    }

    /**
     * 唯一标示
     *
     * @return
     */
    private static String getNextTimestamp() {
        return Long.toHexString(System.currentTimeMillis());
    }
}
