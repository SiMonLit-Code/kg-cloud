package ai.plantdata.kgcloud.domain.edit.req.attr;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.Map;

/**
 * @Author: LinHo
 * @Date: 2019/12/18 16:02
 * @Description:
 */
@Getter
@Setter
@ApiModel("关系溯源条件查询")
public class RelationSearchMetaReq {
    @ApiModelProperty(value = "来源")
    @Length(max = 50, message = "长度不能超过50")
    private String source;

    @ApiModelProperty(value = "批次号")
    private String batchNo;

    @ApiModelProperty(value = "置信度筛选,{$gt:0}")
    private Map<String, Object> reliability;


    /**
     * 来源操作者
     */
    @ApiModelProperty(value = "来源操作者")
    @Length(max = 50, message = "长度不能超过50")
    private String sourceUser="";

    /**
     * 来源动作
     */
    @ApiModelProperty(value = "来源动作")
    @Length(max = 50, message = "长度不能超过50")
    private String sourceAction="";
}
