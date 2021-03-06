package ai.plantdata.kgcloud.sdk.rsp.edit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Map;

/**
 * @Author: LinHo
 * @Date: 2019/11/26 11:46
 * @Description:
 */
@Data
@ApiModel("批量关系新建结果模型")
public class BatchRelationRsp {
    @ApiModelProperty(value = "属性定义id", required = true)
    private Integer attrId;
    @ApiModelProperty(value = "实体id entityId/(entityName+entityMeaningTag)二选一必填", required = true)
    private Long entityId;
    @ApiModelProperty("实体名称 不存在则新增实体")
    @Length(max = 50, message = "长度不能超过50")
    private String entityName;
    @ApiModelProperty("实体消歧标识")
    @Length(max = 50, message = "长度不能超过50")
    private String entityMeaningTag;
    @ApiModelProperty("实体概念")
    private Long entityConcept;
    @ApiModelProperty("终点实体id attrValueId/(attrValueName+attrValueMeaningTag)二选一必填")
    private Long attrValueId;
    @ApiModelProperty("终点实体名称")
    @Length(max = 50, message = "长度不能超过50")
    private String attrValueName;
    @ApiModelProperty("终点实体消歧标识")
    @Length(max = 50, message = "长度不能超过50")
    private String attrValueMeaningTag;
    @ApiModelProperty("终点实体消歧标识")
    private Long attrValueConcept;
    @ApiModelProperty("开始时间")
    private String attrTimeFrom;
    @ApiModelProperty("结束时间")
    private String attrTimeTo;
    @ApiModelProperty("边属性Map,其中key为边数值属性的seqNo，value为对应值key为边对象属性的seqNo，value为对象实例id数组或数值属性")
    private Map<Integer, Object> extraInfoMap;
    @ApiModelProperty("关系id")
    private String id;
    @ApiModelProperty("元数据 无需填写")
    private Map<String, Object> metaData;
    @ApiModelProperty("错误信息 新增无需填写")
    private String note;
}
