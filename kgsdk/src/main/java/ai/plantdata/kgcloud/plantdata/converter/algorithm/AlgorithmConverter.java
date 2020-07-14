package ai.plantdata.kgcloud.plantdata.converter.algorithm;

import ai.plantdata.kgcloud.plantdata.req.rule.BusinessGraphBean;
import ai.plantdata.kgcloud.plantdata.rsp.app.EntityBeanAnalysisa;
import ai.plantdata.kgcloud.plantdata.rsp.app.GraphAnalysisaBean;
import ai.plantdata.kgcloud.plantdata.rsp.app.statistic.AlgorithmStatisticeBean;
import com.google.common.collect.Lists;
import ai.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import ai.plantdata.kgcloud.sdk.req.app.algorithm.BusinessGraphRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.ComplexGraphVisualRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.statistic.AlgorithmStatisticeRsp;
import lombok.NonNull;
import org.apache.commons.lang3.math.NumberUtils;

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

    public static AlgorithmStatisticeBean algorithmStatisticeRspToBean(@NonNull AlgorithmStatisticeRsp statisticeRsp) {
        return copy(statisticeRsp, AlgorithmStatisticeBean.class);
    }

    public static GraphAnalysisaBean complexGraphVisualRspToGraphAnalysisaBean(@NonNull ComplexGraphVisualRsp visualRsp) {
        GraphAnalysisaBean bean = new GraphAnalysisaBean();
        bean.setEntityList(toListNoNull(visualRsp.getEntityList(), AlgorithmConverter::coordinatesEntityRspToEntityBeanAnalysisa));
        return bean;
    }

    private static EntityBeanAnalysisa coordinatesEntityRspToEntityBeanAnalysisa(@NonNull ComplexGraphVisualRsp.CoordinatesEntityRsp entityRsp) {
        EntityBeanAnalysisa analysis = new EntityBeanAnalysisa();
        analysis.setName(entityRsp.getName());
        analysis.setId(entityRsp.getId());
        analysis.setCluster(entityRsp.getCluster());
        analysis.setClassId(entityRsp.getClassId() == null ? NumberUtils.INTEGER_ZERO : entityRsp.getClassId());
        analysis.setClassIdList(Lists.newArrayList(entityRsp.getClassId()));
        analysis.setConceptId(entityRsp.getConceptId());
        analysis.setConceptIdList(entityRsp.getConceptIdList());
        analysis.setCreationTime(entityRsp.getCreationTime());
        consumerIfNoNull(entityRsp.getCoordinates(), a -> {
            analysis.setX(a.getX());
            analysis.setY(a.getY());
        });
        analysis.setDistance(entityRsp.getDistance());
        analysis.setMeaningTag(entityRsp.getMeaningTag());
        return analysis;
    }
}
