package com.plantdata.kgcloud.domain.scene.req;

import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.domain.scene.rsp.ModelAnalysisRsp;
import com.plantdata.kgcloud.domain.scene.rsp.NlpRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.SchemaRsp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
@ApiModel("场景新增")
public class SceneReq extends BaseReq {

    @ApiModelProperty(value = "场景id")
    private Integer id;

    @ApiModelProperty(value = "场景名称")
    private String name;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "场景描述")
    private String abs;

    @ApiModelProperty(value = "是否结构拆解")
    private Boolean structureDismantling;

    @ApiModelProperty(value = "拆解维度 1文章原有结构 2段 3句")
    private Integer dismantlingDimensionality;

    @ApiModelProperty(value = "标注模式")
    private SchemaRsp labelModel;

    @ApiModelProperty(value = "kgName")
    private String kgName;

    @ApiModelProperty(value = "标注模式类型 1从图谱引入 2手动创建")
    private Integer labelModelType;

    @ApiModelProperty(value = "知识库标引状态 1启用 0关闭")
    private Integer knowledgeIndex;

    @ApiModelProperty(value = "模板解析")
    private List<ModelAnalysisRsp> modelAnalysis;

    @ApiModelProperty(value = "nlp能力")
    private List<NlpRsp> nlp;

    @ApiModelProperty(value = "文档格式 1 文档 2 网页")
    private Integer docFormat;

    @ApiModelProperty(value = "文档类型")
    private List<String> docType;

}
