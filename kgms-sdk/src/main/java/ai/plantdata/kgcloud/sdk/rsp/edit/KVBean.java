package ai.plantdata.kgcloud.sdk.rsp.edit;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * @param <T>
 * @param <S>
 * @author Administrator
 */
@Getter
@Setter
public class KVBean<T, S> {
    private T key;
    private S value;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        KVBean<?, ?> kvBean = (KVBean<?, ?>) o;
        return Objects.equals(key, kvBean.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}
