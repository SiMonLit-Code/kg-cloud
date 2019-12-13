package com.plantdata.kgcloud.domain.parse.req;

import com.hiekn.pddocument.bean.element.PdEntity;
import com.plantdata.kgcloud.domain.scene.rsp.NlpRsp;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class NlpParseReq {

    @ApiModelProperty(value = "nlp配置")
    private List<NlpRsp> nlpConfigs;

    @ApiModelProperty(value = "输入文本")
    private List<String> inputs;

    @ApiModelProperty(value = "实体,双重数组，外层数组数量跟文本数组数量必须一致")
    private List<List<PdEntity>> pdEntityList;
}
