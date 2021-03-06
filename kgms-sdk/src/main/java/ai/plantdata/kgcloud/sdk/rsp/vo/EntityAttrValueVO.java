package ai.plantdata.kgcloud.sdk.rsp.vo;

import ai.plantdata.kgcloud.sdk.req.edit.AttrDefinitionVO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Author: LinHo
 * @Date: 2019/11/18 18:05
 * @Description:
 */
@Data
@ApiModel("属性值查询结果模型")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class EntityAttrValueVO extends AttrDefinitionVO {

    @ApiModelProperty(value = "数值属性值")
    private Object dataValue;

    @ApiModelProperty(value = "数值属性TripleId")
    private String dataValueTripleId;

    @ApiModelProperty(value = "对象属性值")
    private List<ObjectAttrValueVO> objectValues;


    @ApiModelProperty(value = "数值属性metaData")
    private Map<String,Object> metaData;

    @ApiModelProperty(value = "是否还有下一页")
    private Boolean hasNext = false;

    /**
     * 来源
     */
    @ApiModelProperty(value = "数值属性来源")
    private String source;

    /**
     * 真实来源
     */
    @ApiModelProperty(value = "数值属性真实来源")
    private Map<String,Object> trueSource;

    /**
     * 来源操作者
     */
    @ApiModelProperty(value = "数值属性来源操作者")
    private String sourceUser;

    /**
     * 来源动作
     */
    @ApiModelProperty(value = "数值属性来源动作")
    private String sourceAction;
}
