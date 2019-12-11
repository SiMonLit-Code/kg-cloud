package com.plantdata.kgcloud.sdk.rsp.edit;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Map;

/**
 * @Author: LinHo
 * @Date: 2019/11/26 11:46
 * @Description:
 */
@Data
@ApiModel("批量关系新建结果.")
public class BatchRelationRsp {
    private Integer attrId;
    private Long entityId;
    private String entityName;
    private String entityMeaningTag;
    private Long entityConcept;
    private Long attrValueId;
    private String attrValueName;
    private String attrValueMeaningTag;
    private Long attrValueConcept;
    private String attrTimeFrom;
    private String attrTimeTo;
    private Map<Integer, String> extraInfoMap;
    private String id;
    private String note;
}
