package com.plantdata.kgcloud.sdk.rsp.app.main;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/21 18:06
 */
@Getter
@Setter
@NoArgsConstructor
public class BasicConceptRsp {
    private Long id;
    private String name;
    private String key;
    private String meaningTag;
    private Long parentId;
    private Integer type;
    private String imgUrl;

}
