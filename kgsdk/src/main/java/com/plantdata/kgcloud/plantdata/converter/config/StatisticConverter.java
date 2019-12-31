package com.plantdata.kgcloud.plantdata.converter.config;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import com.plantdata.kgcloud.plantdata.req.config.InitStatisticalBean;
import com.plantdata.kgcloud.plantdata.req.rule.InitStatisticalBeanAdd;
import com.plantdata.kgcloud.sdk.req.GraphConfStatisticalReq;
import com.plantdata.kgcloud.sdk.req.UpdateGraphConfStatisticalReq;
import com.plantdata.kgcloud.sdk.rsp.GraphConfStatisticalRsp;

import java.util.List;
import java.util.function.Function;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/25 14:01
 */
public class StatisticConverter extends BasicConverter {

    public static Function<List<InitStatisticalBean>, List<UpdateGraphConfStatisticalReq>> updateBeanToReq =
            a -> BasicConverter.toListNoNull(a, StatisticConverter::initStatisticalAddBatchToUpdateGraphConfStatisticalReq);
    public static Function<List<InitStatisticalBean>, List<GraphConfStatisticalReq>> addBeanToReq =
            a -> BasicConverter.toListNoNull(a, StatisticConverter::initStatisticalAddBatchToGraphConfStatisticalReq);
    public static Function<ApiReturn<List<GraphConfStatisticalRsp>>, List<InitStatisticalBean>> rspToBean =
            a -> BasicConverter.convert(a, b -> BasicConverter.toListNoNull(b, StatisticConverter::graphConfStatisticalRspToInitStatisticalBean));

    public static InitStatisticalBean graphConfStatisticalRspToInitStatisticalBean(GraphConfStatisticalRsp rsp) {
        InitStatisticalBean statisticalBean = new InitStatisticalBean();
        statisticalBean.setId(rsp.getId().intValue());
        statisticalBean.setKgName(rsp.getKgName());
        statisticalBean.setType(rsp.getStatisType());
        statisticalBean.setRule(rsp.getStatisRule());
        statisticalBean.setUpdateTime(rsp.getUpdateAt());
        statisticalBean.setCreateTime(rsp.getCreateAt());
        return statisticalBean;
    }

    private static UpdateGraphConfStatisticalReq initStatisticalAddBatchToUpdateGraphConfStatisticalReq(InitStatisticalBean statisticalBean) {
        UpdateGraphConfStatisticalReq statisticalReq = initStatisticalReq(statisticalBean, new UpdateGraphConfStatisticalReq());
        statisticalReq.setId(statisticalBean.getId().longValue());
        return statisticalReq;
    }

    private static GraphConfStatisticalReq initStatisticalAddBatchToGraphConfStatisticalReq(InitStatisticalBean statisticalBean) {
        return initStatisticalReq(statisticalBean, new GraphConfStatisticalReq());
    }


    private static <R extends GraphConfStatisticalReq> R initStatisticalReq(InitStatisticalBean statisticalBean, R req) {
        req.setKgName(statisticalBean.getKgName());
        req.setStatisRule(statisticalBean.getRule());
        req.setStatisType(statisticalBean.getType());
        return req;
    }


    public static GraphConfStatisticalReq initStatisticalBeanAddToGraphConfStatisticalReq(InitStatisticalBeanAdd beanAdd) {
        GraphConfStatisticalReq statisticalReq = new GraphConfStatisticalReq();
        statisticalReq.setKgName(beanAdd.getKgName());
        statisticalReq.setStatisType(beanAdd.getType());
        statisticalReq.setStatisRule(beanAdd.getRule());
        return statisticalReq;
    }

}
