package ai.plantdata.kgcloud.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringBeanUtil implements ApplicationContextAware {
    private static ApplicationContext ac;

    public SpringBeanUtil() {
    }

    public static Object getBean(String name) {
        checkApplicationContext();
        return ac.getBean(name);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        checkApplicationContext();
        return ac.getBean(name, clazz);
    }

    public static <T> T getBean(Class<T> clazz) {
        checkApplicationContext();
        return ac.getBean(clazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ac = applicationContext;
    }

    private static void checkApplicationContext() {
        if (ac == null) {
            throw new IllegalStateException("applicationContext not inject yet");
        }
    }
}