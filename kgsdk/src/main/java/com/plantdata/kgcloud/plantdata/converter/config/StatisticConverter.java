package com.plantdata.kgcloud.plantdata.converter.config;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import com.plantdata.kgcloud.plantdata.req.config.InitStatisticalBean;
import com.plantdata.kgcloud.plantdata.req.rule.InitStatisticalBeanAdd;
import com.plantdata.kgcloud.sdk.req.GraphConfStatisticalReq;
import com.plantdata.kgcloud.sdk.rsp.GraphConfStatisticalRsp;

import java.util.List;
import java.util.function.Function;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/25 14:01
 */
public class StatisticConverter extends BasicConverter {

    public static Function<List<InitStatisticalBean>, List<GraphConfStatisticalReq>> beanToReq =
            a -> BasicConverter.listToRsp(a, StatisticConverter::initStatisticalAddBatchToGraphConfStatisticalReq);
    public static Function<ApiReturn<List<GraphConfStatisticalRsp>>, List<InitStatisticalBean>> rspToBean =
            a -> BasicConverter.convert(a, b -> BasicConverter.listToRsp(b, StatisticConverter::graphConfStatisticalRspToInitStatisticalBean));

    public static InitStatisticalBean graphConfStatisticalRspToInitStatisticalBean(GraphConfStatisticalRsp rsp) {
        InitStatisticalBean statisticalBean = new InitStatisticalBean();
        statisticalBean.setId(rsp.getId().intValue());
        statisticalBean.setKgName(rsp.getKgName());
        statisticalBean.setType(rsp.getStatisType());
        ///todo
        //statisticalBean.setRule(rsp.getStatisRule());
        //statisticalBean.setUpdateTime();
        //statisticalBean.setCreateTime(rsp.get);
        return statisticalBean;
    }

    private static GraphConfStatisticalReq initStatisticalAddBatchToGraphConfStatisticalReq(InitStatisticalBean statisticalBean) {
        GraphConfStatisticalReq statisticalReq = new GraphConfStatisticalReq();
        statisticalReq.setId(statisticalBean.getId().longValue());
        statisticalReq.setKgName(statisticalBean.getKgName());
        ///todo
        //statisticalReq.setStatisRule(statisticalBean.getRule());
        statisticalReq.setStatisType(statisticalBean.getType());
        return statisticalReq;
    }

    public static GraphConfStatisticalReq initStatisticalBeanAddToGraphConfStatisticalReq(InitStatisticalBeanAdd beanAdd) {
        GraphConfStatisticalReq statisticalReq = new GraphConfStatisticalReq();
        statisticalReq.setKgName(beanAdd.getKgName());
        statisticalReq.setStatisType(beanAdd.getType());
        ///todo
        //statisticalReq.setId();
        //statisticalReq.setStatisRule();
        return statisticalReq;
    }

}
