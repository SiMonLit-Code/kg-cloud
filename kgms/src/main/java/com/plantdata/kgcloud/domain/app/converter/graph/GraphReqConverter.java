package com.plantdata.kgcloud.domain.app.converter.graph;

import ai.plantdata.kg.api.pub.req.CommonFilter;
import ai.plantdata.kg.api.pub.req.GraphFrom;
import ai.plantdata.kg.api.pub.req.MetaData;
import ai.plantdata.kg.api.pub.req.PathFrom;
import ai.plantdata.kg.api.pub.req.RelationFrom;
import com.plantdata.kgcloud.constant.MetaDataInfo;
import com.plantdata.kgcloud.sdk.req.app.TimeFilterExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicGraphExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.CommonFiltersReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.CommonPathReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.CommonRelationReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.GraphCommonReqInterface;
import com.plantdata.kgcloud.sdk.req.app.explore.common.GraphPathReqInterface;
import com.plantdata.kgcloud.sdk.req.app.explore.common.GraphRelationReqInterface;
import com.plantdata.kgcloud.sdk.req.app.explore.common.GraphTimingReqInterface;
import com.plantdata.kgcloud.util.DateUtils;
import com.plantdata.kgcloud.util.JacksonUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/28 10:01
 */
@Slf4j
public class GraphReqConverter {

    /**
     * 图探索参数 构建 GraphFrom
     *
     * @param exploreReq 探索的参数
     * @param <E>        。。。
     * @return 。。
     */
    public static <E extends BasicGraphExploreReq> GraphFrom commonReqProxy(E exploreReq) {
        GraphFrom graphFrom = GraphCommonConverter.basicReqToRemote(exploreReq.getPage(), exploreReq, new GraphFrom());
        if (exploreReq instanceof GraphCommonReqInterface) {
            fillCommon(((GraphCommonReqInterface) exploreReq).fetchCommon(), graphFrom);
        }
        if (exploreReq instanceof GraphTimingReqInterface) {
            fillTimeFilters(((GraphTimingReqInterface) exploreReq).fetchTimeFilter(), graphFrom);
        }
        log.info("commonReqProxy:{}", JacksonUtils.writeValueAsString(exploreReq));
        return graphFrom;
    }

    /**
     * 路径分析参数 构建 PathFrom
     *
     * @param exploreReq 探索的参数
     * @param <E>        。。。
     * @return 。。
     */
    public static <E extends BasicGraphExploreReq> PathFrom pathReqProxy(E exploreReq) {
        PathFrom pathFrom = GraphCommonConverter.basicReqToRemote(exploreReq.getPage(), exploreReq, new PathFrom());
        if (exploreReq instanceof GraphPathReqInterface) {
            fillPath(((GraphPathReqInterface) exploreReq).fetchPath(), pathFrom);
        }
        if (exploreReq instanceof GraphTimingReqInterface) {
            fillTimeFilters(((GraphTimingReqInterface) exploreReq).fetchTimeFilter(), pathFrom);
        }
        log.info("pathReqProxy:{}", JacksonUtils.writeValueAsString(exploreReq));
        return pathFrom;
    }

    /**
     * 路径分析参数 构建 PathFrom
     *
     * @param exploreReq 探索的参数
     * @param <E>        。。。
     * @return 。。
     */
    public static <E extends BasicGraphExploreReq> RelationFrom relationReqProxy(E exploreReq) {
        RelationFrom relationFrom = GraphCommonConverter.basicReqToRemote(exploreReq.getPage(), exploreReq, new RelationFrom());
        //关联分析
        if (exploreReq instanceof GraphRelationReqInterface) {
            fillRelation(((GraphRelationReqInterface) exploreReq).fetchRelation(), relationFrom);
        }
        if (exploreReq instanceof GraphTimingReqInterface) {
            fillTimeFilters(((GraphTimingReqInterface) exploreReq).fetchTimeFilter(), relationFrom);
        }
        log.info("relationReqProxy:{}", JacksonUtils.writeValueAsString(exploreReq));
        return relationFrom;
    }


    private static void fillRelation(CommonRelationReq relation, RelationFrom graphFrom) {
        graphFrom.setIds(relation.getIds());
        graphFrom.setDistance(relation.getDistance());
    }

    private static void fillCommon(CommonFiltersReq common, GraphFrom graphFrom) {
        graphFrom.setId(common.getId());
        graphFrom.setName(common.getKw());
        graphFrom.setQueryPrivate(common.isPrivateAttRead());
    }

    private static void fillPath(CommonPathReq pathReq, PathFrom pathFrom) {
        pathFrom.setStart(pathReq.getStart());
        pathFrom.setEnd(pathReq.getEnd());
        pathFrom.setDistance(pathReq.getDistance());
        pathFrom.setShortest(pathReq.isShortest());
    }

    private static <T extends CommonFilter> void fillTimeFilters(TimeFilterExploreReq timeFilter, T commonFilter) {
        //实体时间筛选
        if (timeFilter == null) {
            return;
        }
        String fromTime = null;
        String toTime = null;
        Map<String, String> timeRangeMap = new HashMap<>(2);
        if (timeFilter.getFromTime() != null) {
            fromTime = DateUtils.formatDate(timeFilter.getFromTime());
            timeRangeMap.put("$gte", fromTime);
        }
        if (timeFilter.getToTime() != null) {
            toTime = DateUtils.formatDate(timeFilter.getToTime());
            timeRangeMap.put("$lte", toTime);
        }
        Map<Integer, Object> entityFromTimeMap = new HashMap<>(2);
        entityFromTimeMap.put(Integer.parseInt(MetaDataInfo.FROM_TIME.getCode()), timeRangeMap);

        Map<Integer, Integer> entityFromTimeSortMap = new HashMap<>(1);
        entityFromTimeSortMap.put(Integer.parseInt(MetaDataInfo.FROM_TIME.getCode()), timeFilter.getSort().getValue());
        MetaData entityMetaData = commonFilter.getEntityMeta();
        MetaData relationMetaData = commonFilter.getRelationMeta();
        switch (timeFilter.getTimeFilterType()) {
            case 0:
                break;
            case 1:
                entityMetaData.setSort(entityFromTimeSortMap);
                entityMetaData.setFilter(entityFromTimeMap);
                break;
            case 2:
                relationMetaData.setSort(entityFromTimeSortMap);
                relationMetaData.setFilter(entityFromTimeMap);
                break;
            case 3:
                commonFilter.setAttrTimeFrom(fromTime);
                commonFilter.setAttrTimeTo(toTime);
                entityMetaData.setSort(entityFromTimeSortMap);
                entityMetaData.setFilter(entityFromTimeMap);
                relationMetaData.setSort(entityFromTimeSortMap);
                relationMetaData.setFilter(entityFromTimeMap);
                break;
            default:
        }
    }
}
