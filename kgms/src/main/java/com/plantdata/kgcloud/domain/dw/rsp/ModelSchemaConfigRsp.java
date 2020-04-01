package com.plantdata.kgcloud.domain.dw.rsp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModelSchemaConfigRsp {

    private String tableName;

    private Integer modelId;

    private Set<String> entity;

    private Set<RelationBean> relation;

    private Set<AttrBean> attr;

    private Boolean syns = false;

    @Data
    public static class RelationBean {
        private String name;

        private String domain;

        private Set<String> range;

        private Set<RelationAttr> attrs;

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            RelationBean that = (RelationBean) o;
            return Objects.equals(name, that.name) &&
                    Objects.equals(domain, that.domain);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, domain);
        }
    }

    @Data
    public static class AttrBean {
        private String name;

        private String domain;

        private Integer dataType;

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            AttrBean attrBean = (AttrBean) o;
            return Objects.equals(name, attrBean.name) &&
                    Objects.equals(domain, attrBean.domain);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, domain);
        }
    }

    @Data
    public static class RelationAttr {
        private String name;

        private Integer dataType;

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            RelationAttr that = (RelationAttr) o;
            return Objects.equals(name, that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }
}

