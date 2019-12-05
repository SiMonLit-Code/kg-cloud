package com.plantdata.kgcloud.domain.common.rsp.matedata;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/4 10:48
 */
@Getter
@Setter
@ToString
@ApiModel("关联实体")
public class EntityLinkRsp {

    private String kgTitle;
    private String kgName;
    private String entityName;
    private Long entityId;
}
