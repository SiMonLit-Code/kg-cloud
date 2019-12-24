package com.plantdata.kgcloud.plantdata.req.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
@Getter
@Setter
@NoArgsConstructor
public class KVBean<W, T> {

    private W k;
    private T v;
    private Integer attDefid;
    private Integer type;
    private Long domain;


    public KVBean(W k, T v, Integer attDefid) {
        this.k = k;
        this.v = v;
        this.attDefid = attDefid;
    }

    public KVBean(W k, T v) {
        this.k = k;
        this.v = v;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KVBean<?, ?> kvBean = (KVBean<?, ?>) o;
        return Objects.equals(k, kvBean.k);
    }

    @Override
    public int hashCode() {
        return Objects.hash(k);
    }
}
