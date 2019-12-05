package com.plantdata.kgcloud.sdk.rsp.app.main;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/21 17:59
 */
@Getter
@Setter
public class BasicConceptTreeRsp extends BasicConceptRsp {
    private List<BasicConceptTreeRsp> children;
    private List<NumberAttr> numAttrs;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NumberAttr {
        private Integer id;
        private String name;
        private Long conceptId;
        private Integer dataType;
    }
}
