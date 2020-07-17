package ai.plantdata.kgcloud.sdk.req.app.infobox;

import ai.plantdata.kgcloud.sdk.req.app.function.AttrDefListKeyReqInterface;
import ai.plantdata.kgcloud.sdk.validator.ChooseCheck;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/4 10:10
 */
@ApiModel("知识卡片-参数(批量)")
@Getter
@Setter
public class BatchInfoBoxReqList implements AttrDefListKeyReqInterface {
    @ApiModelProperty("实体id、概念id")
    private List<Long> ids;
    @ApiModelProperty("实体名称,当id为空时生效")
    private List<String> kws;
    @ApiModelProperty(value = "是否读取对象属性,默认false")
    @ChooseCheck(value = "[true,false]", type = String.class, name = "relationAttrs", isBlank = true)
    private Boolean relationAttrs = false;
    @ApiModelProperty("是否读取反向对象属性,默认false")
    @ChooseCheck(value = "[true,false]", type = String.class, name = "relationAttrs", isBlank = true)
    private Boolean reverseRelationAttrs = false;
    @ApiModelProperty("查询指定的属性，格式为json数组格式 例：[1,2]，默认为读取全部")
    private List<Integer> allowAttrs;
    @ApiModelProperty("指定的属性的唯一标识 allowAttrs为空时生效 例：[\"key1\",\"key2\"]")
    private List<String> allowAttrsKey;

}
