package com.plantdata.kgcloud.sdk.rsp.app.main;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/11 14:33
 */
@ApiModel
@Getter
@Setter
public class EntityLinkRsp {
    private String kgTitle;
    private String kgName;

    private String entityName;
    private Long entityId;
}
