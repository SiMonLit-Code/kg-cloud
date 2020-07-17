package ai.plantdata.kgcloud.config;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/26 11:25
 */
public class CurrentUser {
    private static ThreadLocal<Boolean> adminLocal = new ThreadLocal<>();

    private static ThreadLocal<String> tokenLocal = new ThreadLocal<>();

    public static boolean isAdmin() {
        return adminLocal.get() != null && adminLocal.get();
    }

    public static void setAdmin(Boolean admin) {
        adminLocal.set(admin);
    }

    public static String getToken() {
        return tokenLocal.get();
    }

    public static void setToken(String token) {
        tokenLocal.set(token);
    }
}
