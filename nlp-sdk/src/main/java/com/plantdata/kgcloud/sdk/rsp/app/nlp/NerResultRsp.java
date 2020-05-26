package com.plantdata.kgcloud.sdk.rsp.app.nlp;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/4 17:36
 */
@Getter
@Setter
public class NerResultRsp {
    private String tag;
    private List<NamedEntityRsp> entityList;
}
