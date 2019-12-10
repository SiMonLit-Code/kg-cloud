package com.plantdata.kgcloud.domain.d2r.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author xiezhenxiang 2019/12/9
 */
@Data
@ApiModel
public class SynonymRsp {

    @JsonProperty("ent_id")
    private Long entId;
    private Long cid;
    @JsonProperty("update_opt")
    private int updateOpt;
    private String synonym;
    private String name;
}
