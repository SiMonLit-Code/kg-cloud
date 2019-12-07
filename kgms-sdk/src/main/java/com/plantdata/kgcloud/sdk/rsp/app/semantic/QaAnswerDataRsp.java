package com.plantdata.kgcloud.sdk.rsp.app.semantic;

import com.fasterxml.jackson.databind.node.ArrayNode;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Administrator
 */
@Getter
@Setter
@ApiModel("问答结果")
public class QaAnswerDataRsp {
    /**
     * 是否命中意图 0 未识别意图 1 识别意图
     */
    private int hit;
    /**
     * 结果类型 text 文本 textarray 文本列表 objectarray 节点列表
     */
    private String type;
    private String text;
    private int count;
    private ArrayNode array;
    private ArrayNode word;


}
