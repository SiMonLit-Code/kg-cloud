package ai.plantdata.kgcloud.plantdata.converter.config;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.kgcloud.plantdata.bean.RelationbyFilterBean;
import ai.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import ai.plantdata.kgcloud.plantdata.req.config.InitStatisticalBean;
import ai.plantdata.kgcloud.plantdata.req.rule.InitStatisticalBeanAdd;
import com.google.common.collect.Lists;
import ai.plantdata.kgcloud.plantdata.req.data.StatCountRelationbyEntityParameter;
import ai.plantdata.kgcloud.plantdata.req.data.StatEdgeGroupByEdgeValueParameter;
import ai.plantdata.kgcloud.plantdata.req.data.StatEntityGroupByAttributeByConceptIdParameter;
import ai.plantdata.kgcloud.plantdata.req.data.StatEntityGroupByAttrvalueByAttrIdParameter;
import ai.plantdata.kgcloud.plantdata.req.data.StatEntityGroupByConceptParameter;
import ai.plantdata.kgcloud.sdk.req.GraphConfStatisticalReq;
import ai.plantdata.kgcloud.sdk.req.UpdateGraphConfStatisticalReq;
import ai.plantdata.kgcloud.sdk.req.app.statistic.DateTypeReq;
import ai.plantdata.kgcloud.sdk.req.app.statistic.EdgeAttrStatisticByAttrValueReq;
import ai.plantdata.kgcloud.sdk.req.app.statistic.EdgeStatisticByConceptIdReq;
import ai.plantdata.kgcloud.sdk.req.app.statistic.EdgeStatisticByEntityIdReq;
import ai.plantdata.kgcloud.sdk.req.app.statistic.EntityStatisticGroupByAttrIdReq;
import ai.plantdata.kgcloud.sdk.req.app.statistic.EntityStatisticGroupByConceptReq;
import ai.plantdata.kgcloud.sdk.req.app.statistic.IdsFilterReq;
import ai.plantdata.kgcloud.sdk.rsp.GraphConfStatisticalRsp;
import lombok.NonNull;
import org.springframework.beans.BeanUtils;

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
        statisticalBean.setId(rsp.getId());
        statisticalBean.setKgName(rsp.getKgName());
        statisticalBean.setType(rsp.getStatisType());
        statisticalBean.setRule(rsp.getStatisRule());
        statisticalBean.setUpdateTime(rsp.getUpdateAt());
        statisticalBean.setCreateTime(rsp.getCreateAt());
        return statisticalBean;
    }

    public static EdgeStatisticByEntityIdReq statCountRelationbyEntityParameterToEdgeStatisticByEntityIdReq(@NonNull StatCountRelationbyEntityParameter param) {
        EdgeStatisticByEntityIdReq req = new EdgeStatisticByEntityIdReq();
        consumerIfNoNull(param.getIsDistinct(),req::setDistinct);
        req.setEntityId(param.getEntityId());
        consumerIfNoNull(toListNoNull(param.getAllowAtts(), StatisticConverter::relationbyFilterBeanToIdsFilterReq), req::setAllowAttrDefIds);
        consumerIfNoNull(toListNoNull(param.getAllowTypes(), StatisticConverter::relationbyFilterBeanToIdsFilterReq), req::setAllowConceptIds);
        return req;
    }

    public static EdgeStatisticByConceptIdReq statEntityGroupByAttributeByConceptIdParameterToEdgeStatisticByConceptIdReq(@NonNull StatEntityGroupByAttributeByConceptIdParameter param) {
        EdgeStatisticByConceptIdReq req = new EdgeStatisticByConceptIdReq();
        req.setConceptId(param.getConceptId());
        req.setConceptKey(param.getConceptKey());
        req.setAllowAttrs(param.getAllowAtts());
        req.setAllowAttrsKey(param.getAllowAttsKey());
        req.setFromTime(param.getFromTime());
        req.setReturnType(param.getReturnType());
        req.setSize(param.getSize());
        req.setSort(param.getSort());
        req.setToTime(param.getToTime());
        req.setTripleIds(param.getTripleIds());
        return req;
    }

    public static EdgeAttrStatisticByAttrValueReq statEdgeGroupByEdgeValueParameterToEdgeAttrStatisticByAttrValueReq(StatEdgeGroupByEdgeValueParameter param) {
        EdgeAttrStatisticByAttrValueReq req = new EdgeAttrStatisticByAttrValueReq();
        req.setAttrDefId(param.getAttrId());
        req.setAttrDefKey(param.getAttrKey());
        consumerIfNoNull(param.getDateType(), a -> {
            DateTypeReq dateTypeReq = new DateTypeReq();
            dateTypeReq.setType(a.getType());
            dateTypeReq.set$gte(a.get$gte());
            dateTypeReq.set$lte(a.get$lte());
            req.setDateType(dateTypeReq);
        });
        BasicConverter.consumerIfNoNull(param.getEntityIds(),
                a->req.setEntityIds(Lists.newArrayList(a)));
        req.setAttrDefKey(param.getAttrKey());
        req.setMerge(param.getIsMerge());
        req.setReturnType(param.getReturnType());
        req.setSeqNo(param.getSeqNo());
        req.setSize(param.getSize());
        req.setSort(param.getSort());
        req.setTripleIds(param.getTripleIds());
        return req;
    }

    public static EntityStatisticGroupByAttrIdReq statEntityGroupByAttrvalueByAttrIdParameterToEntityStatisticGroupByAttrIdReq(StatEntityGroupByAttrvalueByAttrIdParameter param) {
        EntityStatisticGroupByAttrIdReq req = new EntityStatisticGroupByAttrIdReq();
        req.setEntityIds(param.getEntityIds());
        req.setAttrDefId(param.getAttrId());
        req.setAttrDefKey(param.getAttrKey());
        consumerIfNoNull(param.getDateType(), a -> {
            DateTypeReq dateTypeReq = new DateTypeReq();
            BeanUtils.copyProperties(a, dateTypeReq);
            req.setDataType(dateTypeReq);
        });
        req.setMerge(param.isMerge());
        req.setReturnType(param.getReturnType());
        req.setSize(param.getSize());
        req.setSort(param.getSort());
        return req;
    }


    public static EntityStatisticGroupByConceptReq statEntityGroupByConceptParameterToEntityStatisticGroupByConceptReq(StatEntityGroupByConceptParameter param) {
        EntityStatisticGroupByConceptReq conceptReq = new EntityStatisticGroupByConceptReq();
        conceptReq.setAllowConcepts(param.getAllowTypes());
        conceptReq.setAllowConceptsKey(param.getAllowTypesKey());
        consumerIfNoNull(param.getSort(), conceptReq::setSort);
        conceptReq.setEntityIds(param.getEntityIds());
        conceptReq.setReturnType(param.getReturnType());
        conceptReq.setSize(param.getSize());
        return conceptReq;
    }

    private static <T> IdsFilterReq<T> relationbyFilterBeanToIdsFilterReq(@NonNull RelationbyFilterBean<T> filterBean) {
        IdsFilterReq<T> filterReq = new IdsFilterReq<>();
        filterReq.setIds(filterBean.getIds());
        filterReq.setLayer(filterBean.getLayer());
        return filterReq;
    }

    private static UpdateGraphConfStatisticalReq initStatisticalAddBatchToUpdateGraphConfStatisticalReq(InitStatisticalBean statisticalBean) {
        UpdateGraphConfStatisticalReq statisticalReq = initStatisticalReq(statisticalBean, new UpdateGraphConfStatisticalReq());
        statisticalReq.setId(statisticalBean.getId());
        return statisticalReq;
    }

    private static GraphConfStatisticalReq initStatisticalAddBatchToGraphConfStatisticalReq(InitStatisticalBean statisticalBean) {
        return initStatisticalReq(statisticalBean, new GraphConfStatisticalReq());
    }


    private static <R extends GraphConfStatisticalReq> R initStatisticalReq(InitStatisticalBean statisticalBean, R req) {
        req.setKgName(statisticalBean.getKgName());
        req.setStatisticRule(statisticalBean.getRule());
        req.setStatisticType(statisticalBean.getType());
        return req;
    }


    public static GraphConfStatisticalReq initStatisticalBeanAddToGraphConfStatisticalReq(InitStatisticalBeanAdd beanAdd) {
        GraphConfStatisticalReq statisticalReq = new GraphConfStatisticalReq();
        statisticalReq.setKgName(beanAdd.getKgName());
        statisticalReq.setStatisticType(beanAdd.getType());
        statisticalReq.setStatisticRule(beanAdd.getRule());
        return statisticalReq;
    }

}
