package com.plantdata.kgcloud.sdk.rsp.edit;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(key.toString()).append(value).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this.key.equals(obj);
    }
}
