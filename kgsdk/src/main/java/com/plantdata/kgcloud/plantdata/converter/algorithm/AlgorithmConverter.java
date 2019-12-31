package com.plantdata.kgcloud.plantdata.converter.algorithm;

import com.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import com.plantdata.kgcloud.plantdata.req.rule.BusinessGraphBean;
import com.plantdata.kgcloud.sdk.req.app.algorithm.BusinessGraphRsp;
import lombok.NonNull;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/31 10:55
 */
public class AlgorithmConverter extends BasicConverter {

    public static BusinessGraphRsp businessGraphBeanToRsp(@NonNull BusinessGraphBean graphBean) {
        return copy(graphBean, BusinessGraphRsp.class);
    }

    public static BusinessGraphBean businessGraphRspToBean(@NonNull BusinessGraphRsp graphRsp) {
        return copy(graphRsp, BusinessGraphBean.class);
    }
}
