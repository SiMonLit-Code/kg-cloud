package ai.plantdata.kgcloud.sdk.req.app;


import ai.plantdata.kgcloud.sdk.req.app.function.ConceptKeyListReqInterface;
import ai.plantdata.kgcloud.sdk.req.app.function.PromptSearchInterface;
import ai.plantdata.kgcloud.sdk.validator.ChooseCheck;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/10/31 16:16
 */
@Getter
@Setter
@ApiModel("实体提示参数")
public class PromptReq extends PageReq implements PromptSearchInterface, ConceptKeyListReqInterface {

    @ApiModelProperty("关键字")
    private String kw;
    @ApiModelProperty("类型，默认实体或概念entity_concept； entity：实体； concept:概念 ")
    private String type;
    @ApiModelProperty("查询指定的概念，格式为json数组，默认为查询全部")
    private List<Long> conceptIds = new ArrayList<>();
    @ApiModelProperty("conceptIds为空时此参数生效")
    private List<String> conceptKeys = new ArrayList<>();
    @ApiModelProperty("提示类型 默认0")
    private int promptType;
    @ApiModelProperty("allowTypes字段指定的概念是否继承")
    private Boolean inherit = false;
    @ApiModelProperty("是否大小写敏感（默认不区分大小写")
    private Boolean caseInsensitive = true;
    @ApiModelProperty("是否模糊搜索 false前缀搜索，true支持模糊搜索")
    private boolean fuzzy;
    @ApiModelProperty("是否使用导出实体数据集检索")
    private Boolean openExportDate = true;
    @ApiModelProperty("排序")
    @ChooseCheck(value = "[0,1,-1]", name = "sort", isBlank = true)
    private Integer sort=-1;
    @ApiModelProperty("是否返回顶层概念")
    private Boolean isReturnTop = true;



    @ApiModelProperty(hidden = true)
    @Override
    public Boolean getInherit() {
        return inherit;
    }

    @ApiModelProperty(hidden = true)
    @Override
    public List<Long> getAllowConcepts() {
        return conceptIds;
    }

    @ApiModelProperty(hidden = true)
    @Override
    public void setAllowConcepts(List<Long> allowConceptIds) {
        this.conceptIds = allowConceptIds;
    }

    @ApiModelProperty(hidden = true)
    @Override
    public List<String> getAllowConceptsKey() {
        return conceptKeys;
    }
}
