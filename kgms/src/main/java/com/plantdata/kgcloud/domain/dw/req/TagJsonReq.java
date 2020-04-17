package com.plantdata.kgcloud.domain.dw.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class TagJsonReq {

    @ApiModelProperty("表名")
    private String tableName;

    @ApiModelProperty("概念")
    private Set<String> entity;

    @ApiModelProperty("关系")
    private Set<RelationBean> relation;

    @ApiModelProperty("属性")
    private Set<AttrBean> attr;

    @Data
    public static class RelationBean {

        @ApiModelProperty("关系名")
        private String name;

        @ApiModelProperty("关系定义域")
        private String domain;

        @ApiModelProperty("关系值域")
        private Set<String> range;

        @ApiModelProperty("边属性")
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
        @ApiModelProperty("属性名")
        private String name;

        @ApiModelProperty("属性定义域")
        private String domain;

        @ApiModelProperty("属性类型")
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

        @ApiModelProperty("边属性名")
        private String name;

        @ApiModelProperty("边属性类型")
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

