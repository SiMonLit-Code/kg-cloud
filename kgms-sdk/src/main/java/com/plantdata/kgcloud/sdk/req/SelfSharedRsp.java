package com.plantdata.kgcloud.sdk.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @description:
 * @author: Bovin
 * @create: 2020-01-16 18:15
 **/

@Data
public class SelfSharedRsp {

    @JsonProperty("is_login")
    private Boolean login;

    @JsonProperty("is_self")
    private Boolean self;

    @JsonProperty("share_permission")
    private Boolean sharePermission;

    @JsonProperty("shareable")
    private Boolean shareable;

}
