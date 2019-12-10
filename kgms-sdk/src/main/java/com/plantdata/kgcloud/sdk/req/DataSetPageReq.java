package com.plantdata.kgcloud.sdk.req;

import com.plantdata.kgcloud.bean.BaseReq;
import lombok.Data;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-06 13:07
 **/

@Data
public class DataSetPageReq extends BaseReq {

    private Long folderId;

    private Integer createWay;
}
