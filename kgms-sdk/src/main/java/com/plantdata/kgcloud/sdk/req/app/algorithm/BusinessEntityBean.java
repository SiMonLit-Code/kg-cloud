package com.plantdata.kgcloud.sdk.req.app.algorithm;

import com.plantdata.kgcloud.sdk.rsp.app.explore.TagRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.EntityLinkRsp;
import com.plantdata.kgcloud.sdk.rsp.edit.KVBean;
import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


/**
 * @author Administrator
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@ApiModel("复杂图算法实体参数")
public class BusinessEntityBean {

    private Map<String, Object> style;
    private Long id;
    private String name;
    private Long classId;
    private Long conceptId;
    private String conceptName;
    private String className;
    private String meaningTag;
    /**
     * 0概念 1实例 默认实例
     */
    private Integer type = 1;
    private String img;
    private Double score = 0.0;
    private List<TagRsp> tags;
    private Set<KVBean<String, Object>> extra;
    private String fromTime;
    private String toTime;

    private Set<EntityLinkRsp> entityLinks = new HashSet<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BusinessEntityBean that = (BusinessEntityBean) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
