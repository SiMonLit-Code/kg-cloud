package com.plantdata.kgcloud.domain.app.rsp;

import com.alibaba.fastjson.JSONArray;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/14 16:04
 */
@Getter
@Setter
@ApiModel("问答结果")
public class SemanticAnswerReq {
    @ApiModelProperty("是否命中意图 0 未识别意图 1 识别意图")
    private int hit;
    @ApiModelProperty("结果类型 text 文本 textarray 文本列表 objectarray 节点列表")
    private String type;
    private String text;
    private int count;
    private JSONArray array;
    private JSONArray word;
}
