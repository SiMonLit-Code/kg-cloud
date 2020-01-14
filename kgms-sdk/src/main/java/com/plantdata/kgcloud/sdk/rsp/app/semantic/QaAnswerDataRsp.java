package com.plantdata.kgcloud.sdk.rsp.app.semantic;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.configurationprocessor.json.JSONArray;

import java.util.List;

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
    private List<Object> array;
    private List<Object> word;


}
