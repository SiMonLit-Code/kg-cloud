package com.plantdata.kgcloud.domain.d2r.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author xiezhenxiang 2019/12/9
 */
@Data
@ApiModel
public class AttrRsp {

    @JsonProperty("ent_id")
    private Long entId;
    private Long cid;
    @JsonProperty("update_opt")
    private int updateOpt;
    @JsonProperty("attr_id")
    private int attrId;
    @JsonProperty("attr_value")
    private String attrValue;
    private String name;
    @JsonProperty("attr_name")
    private String attrName;
}
