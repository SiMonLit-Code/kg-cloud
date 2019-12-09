package com.plantdata.kgcloud.sdk.rsp.data;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/7 13:47
 */
@Getter
@Setter
@ApiModel("")
public class RelationUpdateReq {

    private Integer attrId;
    private String tripleId;
    private String attrTimeFrom;
    private String attrTimeTo;
    private Map<Integer, String> extraInfoMap;
    private String note;
}
