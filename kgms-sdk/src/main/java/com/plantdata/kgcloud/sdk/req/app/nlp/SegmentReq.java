package com.plantdata.kgcloud.sdk.req.app.nlp;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/4 18:25
 */
@Getter
@Setter
@ApiModel("分词参数")
public class SegmentReq {
    @NotBlank
    private String kw;

    private Boolean useConcept = true;

    private Boolean useEntity = true;

    private Boolean useAttr = true;
}
