package ai.plantdata.kgcloud.sdk.rsp.app.semantic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Administrator
 */
@Getter
@Setter
@ApiModel("问答结果")
public class QaAnswerDataRsp {
    @ApiModelProperty("是否命中意图 0 未识别意图 1 识别意图")
    private int hit;
   @ApiModelProperty("结果类型 text 文本 textarray 文本列表 objectarray 节点列表")
    private String type;
    @ApiModelProperty("单条文本答案结果")
    private String text;
    @ApiModelProperty("结果集大小")
    private int count;
    @ApiModelProperty("结果集，当type为textarray时类型为string数组，当type为objectarray时为object数组")
    private List<Object> array;
    private List<Object> word;


}
