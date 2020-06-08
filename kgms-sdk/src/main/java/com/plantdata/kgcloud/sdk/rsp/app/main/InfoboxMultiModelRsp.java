package com.plantdata.kgcloud.sdk.rsp.app.main;

import com.plantdata.kgcloud.sdk.rsp.app.explore.BasicEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.edit.MultiModalRsp;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-06-07 13:42
 **/
@Setter
@Getter
@ToString
public class InfoboxMultiModelRsp extends BasicEntityRsp {

    @ApiModelProperty(value = "多模态数据")
    private List<MultiModalRsp> multiModals;
}
