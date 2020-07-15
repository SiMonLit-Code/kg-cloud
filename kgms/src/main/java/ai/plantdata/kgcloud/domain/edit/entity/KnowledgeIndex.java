package ai.plantdata.kgcloud.domain.edit.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author: lp
 **/
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KnowledgeIndex {

    private String id;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("关键词")
    private String keyword;

    @ApiModelProperty("简介")
    private String description;

    @ApiModelProperty("链接")
    private String url;

    @ApiModelProperty("用户ID")
    private String userId;

    @ApiModelProperty("图谱名称")
    private String kgName;

    @ApiModelProperty("标引类型(0：文件,1：文本,2：链接)")
    private Integer indexType;

    @ApiModelProperty("创建时间")
    private Date createTime;

}
