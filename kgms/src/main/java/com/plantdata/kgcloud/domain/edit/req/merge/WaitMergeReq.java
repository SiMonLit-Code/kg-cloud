package com.plantdata.kgcloud.domain.edit.req.merge;

import ai.plantdata.cloud.bean.BaseReq;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Bovin
 * @description
 * @since 2020-05-26 17:11
 **/
@Getter
@Setter
public class WaitMergeReq extends BaseReq {

    private int jobId;
}
