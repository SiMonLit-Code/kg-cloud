package ai.plantdata.kgcloud.domain.app.util;

import ai.plantdata.cloud.exception.BizException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author cjw
 * @date 2020/8/4  11:18
 */
public class AsyncUtils {

    public static <T, R> Supplier<R> async(Function<T, R> a, T param) {
        CompletableFuture<R> cfQueryFromSina = CompletableFuture.supplyAsync(() -> a.apply(param));
        return () -> {
            try {
                return cfQueryFromSina.get();
            } catch (InterruptedException | ExecutionException e) {
                if (e.getCause() instanceof BizException) {
                    throw (BizException) e.getCause();
                }
                e.printStackTrace();
            }
            return null;
        };
    }

    public static  <R> Supplier<R> async(Supplier<R> supplier) {
        return async(a->supplier.get(),null);
    }
}
