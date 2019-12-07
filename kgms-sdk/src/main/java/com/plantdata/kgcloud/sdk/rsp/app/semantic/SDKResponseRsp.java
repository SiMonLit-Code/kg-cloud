package com.plantdata.kgcloud.sdk.rsp.app.semantic;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Administrator
 */
@ApiModel("SDKResponseRsp")
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SDKResponseRsp {

    private String code;
    private String msg;
    private Object data;


}
