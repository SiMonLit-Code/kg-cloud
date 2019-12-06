package com.plantdata.kgcloud.sdk.req.app.nlp;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/6 18:32
 */
@Getter
@Setter
public class EntityLinkingReq {

    private List<Long> conceptIds;
    @NotEmpty
    private String text;
}
