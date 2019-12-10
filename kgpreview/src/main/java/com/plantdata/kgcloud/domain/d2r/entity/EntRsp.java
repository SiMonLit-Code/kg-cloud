package com.plantdata.kgcloud.domain.d2r.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author xiezhenxiang 2019/12/9
 */
@Data
@ApiModel
public class EntRsp {

    @JsonProperty("ent_id")
    private Long entId;
    private Long cid;
    @JsonProperty("update_opt")
    private int updateOpt;
    private String name;
    private String img;
    private String abs;
    @JsonProperty("meaning_tag")
    private String meaningTag;
    @JsonProperty("org_id")
    private String d2rOrgId;
}
