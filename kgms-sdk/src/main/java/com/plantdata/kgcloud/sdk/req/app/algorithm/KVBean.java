package com.plantdata.kgcloud.sdk.req.app.algorithm;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Objects;
@NoArgsConstructor
@AllArgsConstructor
public class KVBean<W, T> {

    private W k;
    private T v;
    private Integer attDefid;
    private Integer type;
    private Long domain;
    public W getK() {
        return k;
    }

    public T getV() {
        return v;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setK(W k) {
        this.k = k;
    }

    public void setV(T v) {
        this.v = v;
    }


    public Integer getAttDefid() {
        return attDefid;
    }

    public void setAttDefid(Integer attDefid) {
        this.attDefid = attDefid;
    }

    public Long getDomain() {
        return domain;
    }

    public void setDomain(Long domain) {
        this.domain = domain;
    }

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
