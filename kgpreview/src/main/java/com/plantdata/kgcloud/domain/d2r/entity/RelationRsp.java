package com.plantdata.kgcloud.domain.d2r.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author xiezhenxiang 2019/12/9
 */
@Data
@ApiModel
public class RelationRsp {

    @JsonProperty("ent_id")
    private Long entId;
    private Long cid;
    @JsonProperty("update_opt")
    private int updateOpt;
    @JsonProperty("attr_id")
    private int attrId;
    @JsonProperty("attr_value")
    private Long attrValue;
    @JsonProperty("attr_value_type")
    private Long attrValueType;
    @JsonProperty("rel_attrs")
    private String relAttrStr;
    private String name;
    @JsonProperty("rel_name")
    private String relName;
}
