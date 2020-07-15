package ai.plantdata.kgcloud.sdk.req.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-05-24 13:29
 **/
@Getter
@Setter
@ApiModel("meta_data过滤")
public class MetaDataReq extends CompareFilterReq{

    @ApiModelProperty(value = "meta_data序号",required = true)
    @NonNull
    private Integer seqNo;

}
