package com.plantdata.kgcloud.domain.repo.model.req;

import com.plantdata.kgcloud.domain.repo.model.common.BaseRepositoryVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author cjw
 * @date 2020/5/20  11:20
 */
@Getter
@Setter
@ApiModel("组件更新请求参数")
public class RepositoryUpdateReq extends BaseRepositoryVO {
    @ApiModelProperty("组件id")
    private int id;
}
