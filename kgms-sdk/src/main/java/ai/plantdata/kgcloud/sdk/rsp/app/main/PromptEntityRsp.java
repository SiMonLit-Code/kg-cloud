package ai.plantdata.kgcloud.sdk.rsp.app.main;

import ai.plantdata.kgcloud.sdk.constant.EntityTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;


/**
 * @author cjw
 * @version 1.0
 * @date 2019/10/31 16:13
 */
@ToString
@Getter
@ApiModel("搜索提示实例视图")
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PromptEntityRsp {
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("消歧标识")
    private String meaningTag;
    @ApiModelProperty("图片")
    private String imageUrl;
    @ApiModelProperty("概念id")
    private Long conceptId;
    @ApiModelProperty("0概念 1实例 ")
    private EntityTypeEnum type;
    @ApiModelProperty("分数")
    private Double score;
    @ApiModelProperty("是否问答")
    private boolean qa;
    private Map<String, Object> dataAttributes;
    private Map<String, List<Long>> objectAttributes;
    private Map<String, List<Long>> reverseObjectAttributes;
    private Map<String, String> privateDataAttributes;
    private Map<String, List<Long>> privateObjectAttributes;
    private Map<String, List<Long>> privateReverseObjectAttributes;
    private Map<String, Object> metaData;

    public PromptEntityRsp(Long id, String name, String meaningTag, Long conceptId, EntityTypeEnum type) {
        this.id = id;
        this.name = name;
        this.meaningTag = meaningTag;
        this.conceptId = conceptId;
        this.type = type;
    }
    public PromptEntityRsp(Long id, String name, String meaningTag, Long conceptId, EntityTypeEnum type,String imageUrl) {
        this.id = id;
        this.name = name;
        this.meaningTag = meaningTag;
        this.conceptId = conceptId;
        this.type = type;
        this.imageUrl = imageUrl;
    }
}
