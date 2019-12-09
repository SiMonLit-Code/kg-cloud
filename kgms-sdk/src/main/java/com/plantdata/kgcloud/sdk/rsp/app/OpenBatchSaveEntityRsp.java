package com.plantdata.kgcloud.sdk.rsp.app;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/7 11:57
 */
@Getter
@Setter
@ToString
@ApiModel("新增或修改实体(open)")
public class OpenBatchSaveEntityRsp {
    private Long conceptId;
    private String name;
    private String meaningTag;
    private String abs;
    private String imageUrl;
    private Long id;
    private String note;
    private List<String> synonyms;
    private Map<Integer, String> attributes;
    private Map<String, String> privateAttributes;

}
