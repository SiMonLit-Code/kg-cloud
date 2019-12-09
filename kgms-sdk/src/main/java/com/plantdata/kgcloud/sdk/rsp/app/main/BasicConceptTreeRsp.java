package com.plantdata.kgcloud.sdk.rsp.app.main;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/21 17:59
 */
@Getter
@Setter
@NoArgsConstructor
@ApiModel("概念树返回列表")
public class BasicConceptTreeRsp extends BasicConceptRsp {

    @ApiModelProperty("子概念")
    private List<BasicConceptTreeRsp> children;
    @ApiModelProperty("子概念")
    private List<NumberAttr> numAttrs;


    public BasicConceptTreeRsp(Long id, String name) {
        this.children = Lists.newArrayList();
        this.numAttrs = Lists.newArrayList();
        this.setId(id);
        this.setName(name);
    }

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
