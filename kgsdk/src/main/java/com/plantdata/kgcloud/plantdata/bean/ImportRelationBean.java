package com.plantdata.kgcloud.plantdata.bean;

import com.plantdata.kgcloud.plantdata.rsp.MarkObject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author xiaohuqi@126.com 2018/8/13
 * 为导入而生的关系Bean
 **/
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class ImportRelationBean implements MarkObject {

    private String tripleId;
    /**
     * 起始节点ID
     */
    private Long entityId;
    private String entityName;  //起始节点名称
    private String entityMeaningTag;  //起始节点消歧标识
    private Long entityConcept;    //起始节点概念ID

    private Integer attrId; //属性ID

    private Long attrValueId;    //结束节点ID
    private String attrValueName;    //结束节点名称
    private String attrValueMeaningTag;    //结束节点消歧标识
    private Long attrValueConcept;  //结束节点概念ID

    private String attrTimeFrom;    //关系边附加起始时间
    private String attrTimeTo;  //关系边附加结束时间

    private Map<String, Object> extraInfoMap;  //边附加属性

    private Map<String, Object> metaData;

}
