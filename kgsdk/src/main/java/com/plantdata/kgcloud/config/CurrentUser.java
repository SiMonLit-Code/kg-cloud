package com.plantdata.kgcloud.config;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/26 11:25
 */
public class CurrentUser {
    private static ThreadLocal<Boolean> adminLocal = new ThreadLocal();

    public static boolean isAdmin() {
        return adminLocal.get() != null && adminLocal.get();
    }

    public static void setAdmin(Boolean admin) {
        adminLocal.set(admin);
    }
}
