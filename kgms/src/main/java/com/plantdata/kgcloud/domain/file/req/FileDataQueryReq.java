package com.plantdata.kgcloud.domain.file.req;

import com.plantdata.kgcloud.bean.BaseReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lp
 * @date 2020/5/20 17:25
 */
@Data
public class FileDataQueryReq extends BaseReq {

    @ApiModelProperty(value = "文件名")
    private String name;

}
