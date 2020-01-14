package com.plantdata.kgcloud.domain.edit.util;

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

    private static String getBatchNo() {
        return batchNo.get();
    }

    public static String setBatchNo() {
        batchNo.set(getNextTimestamp());
        return batchNo.get();
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
