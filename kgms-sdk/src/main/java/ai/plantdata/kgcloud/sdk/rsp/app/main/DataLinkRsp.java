package ai.plantdata.kgcloud.sdk.rsp.app.main;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 知识卡片接口返回增加字段
 *
 * @author wuyue
 * 2019-11-01 18:23:00
 */
@Getter
@Setter
@ApiModel("实体关联数据集(数据标引)")
public class DataLinkRsp {

    @ApiModelProperty("数据集ID")
    private Long dataSetId;
    @ApiModelProperty("数据集Title")
    private String dataSetTitle;
    @ApiModelProperty("数据集")
    private List<LinksRsp> links;

}



