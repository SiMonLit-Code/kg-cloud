package ai.plantdata.kgcloud.sdk.rsp.app.explore;

import ai.plantdata.kgcloud.sdk.rsp.app.MetaDataInterface;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/12 10:02
 */
@Getter
@Setter
@ApiModel("gis信息")
@AllArgsConstructor
@NoArgsConstructor
public class GisInfoRsp implements MetaDataInterface {

    @ApiModelProperty("经度")
    private Double lng;
    @ApiModelProperty("纬度")
    private Double lat;
    @ApiModelProperty("地址名称")
    private String address;
}
