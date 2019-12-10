package com.plantdata.kgcloud.sdk.req;

import com.plantdata.kgcloud.bean.BaseReq;
import lombok.Data;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-09 20:57
 **/
@Data
public class AnnotationQueryReq extends BaseReq {
    private String name;
}
