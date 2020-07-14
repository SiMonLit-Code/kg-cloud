package ai.plantdata.kgcloud.sdk.rsp.edit;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: LinHo
 * @Date: 2020/3/23 10:33
 * @Description:
 */
@Setter
@Getter
public class KnowledgeIndexRsp {

    private String id;

    @ApiModelProperty("实体id")
    private Long entityId;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("关键词")
    private String keyword;

    @ApiModelProperty("简介")
    private String description;

    @ApiModelProperty("链接")
    private String url;

    @ApiModelProperty("标引类型(0：文件,1：文本,2：链接)")
    private Integer indexType;

}
