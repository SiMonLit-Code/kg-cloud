package com.plantdata.kgcloud.sdk.rsp.edit;

import lombok.Getter;
import lombok.Setter;

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
}
