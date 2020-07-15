package ai.plantdata.kgcloud.domain.edit.req.dict;

import ai.plantdata.cloud.bean.BaseReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: LinHo
 * @Date: 2019/12/30 20:05
 * @Description:
 */
@Setter
@Getter
@ApiModel("词典列表")
public class DictSearchReq extends BaseReq {
    @ApiModelProperty(value = "概念id")
    private Long conceptId;

    @ApiModelProperty(value = "词频,-1:降序,1-升序")
    private Integer frequency = 1;
}
