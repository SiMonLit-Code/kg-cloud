package com.plantdata.kgcloud.domain.app.converter.graph;

import ai.plantdata.kg.api.pub.req.CommonFilter;
import ai.plantdata.kg.api.pub.req.GraphFrom;
import ai.plantdata.kg.api.pub.req.MetaData;
import ai.plantdata.kg.api.pub.req.PathFrom;
import ai.plantdata.kg.api.pub.req.RelationFrom;
import com.plantdata.kgcloud.constant.AppErrorCodeEnum;
import com.plantdata.kgcloud.constant.MetaDataInfo;
import com.plantdata.kgcloud.domain.app.converter.BasicConverter;
import com.plantdata.kgcloud.domain.app.converter.ConditionConverter;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.constant.SortTypeEnum;
import com.plantdata.kgcloud.sdk.req.app.TimeFilterExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicGraphExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.CommonFiltersReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.CommonPathReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.CommonRelationReq;
import com.plantdata.kgcloud.sdk.req.app.function.GraphCommonReqInterface;
import com.plantdata.kgcloud.sdk.req.app.function.GraphPathReqInterface;
import com.plantdata.kgcloud.sdk.req.app.function.GraphRelationReqInterface;
import com.plantdata.kgcloud.sdk.req.app.function.GraphTimingReqInterface;
import com.plantdata.kgcloud.util.DateUtils;
import com.plantdata.kgcloud.util.JacksonUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/28 10:01
 */
@Slf4j
public class GraphReqConverter extends BasicConverter {

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
        log.info("graphFrom:{}", JacksonUtils.writeValueAsString(graphFrom));
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
        log.info("pathFrom:{}", JacksonUtils.writeValueAsString(pathFrom));
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
        log.info("relationFrom:{}", JacksonUtils.writeValueAsString(relationFrom));
        return relationFrom;
    }


    private static void fillRelation(@NonNull CommonRelationReq relation, RelationFrom graphFrom) {
        graphFrom.setIds(relation.getIds());
    }

    private static void fillCommon(@NonNull CommonFiltersReq common, GraphFrom graphFrom) {
        if (common.getId() == null && StringUtils.isEmpty(common.getKw())) {
            throw BizException.of(AppErrorCodeEnum.NULL_KW_AND_ID);
        }
        graphFrom.setId(common.getId());
        graphFrom.setName(common.getKw());
        graphFrom.setQueryPrivate(common.isPrivateAttRead());
        graphFrom.getHighLevelFilter().setDirection(common.getDirection());
        graphFrom.getHighLevelFilter().setLimit(common.getHighLevelSize() == null ? graphFrom.getLimit() : common.getHighLevelSize());
        consumerIfNoNull(common.getHyponymyDistance(), graphFrom::setHyponymyDistance);
        if (!CollectionUtils.isEmpty(common.getEdgeAttrSorts())) {
            Map<String, Integer> edgeAttrQuery = ConditionConverter.relationAttrSortToMap(common.getEdgeAttrSorts());
            graphFrom.getHighLevelFilter().setEdgeSort(edgeAttrQuery);
        }
    }

    private static void fillPath(@NonNull CommonPathReq pathReq, PathFrom pathFrom) {
        pathFrom.setStart(pathReq.getStart());
        pathFrom.setEnd(pathReq.getEnd());
        pathFrom.setShortest(pathReq.isShortest());
    }

    private static <T extends CommonFilter> void fillTimeFilters(TimeFilterExploreReq timeFilter, T commonFilter) {
        //实体时间筛选
        if (timeFilter == null) {
            return;
        }
        if (null == timeFilter.getFromTime() && null == timeFilter.getToTime()) {
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
        consumerIfNoNull(timeRangeMap, a -> {
            entityFromTimeMap.put(Integer.parseInt(MetaDataInfo.FROM_TIME.getCode()), a);
        });
        Map<Integer, Integer> entityFromTimeSortMap = new HashMap<>(1);
        consumerIfNoNull(timeFilter.getSort(), a -> {
            Optional<SortTypeEnum> sortTypeEnum = SortTypeEnum.parseByName(timeFilter.getSort());
            entityFromTimeSortMap.put(Integer.parseInt(MetaDataInfo.FROM_TIME.getCode()), sortTypeEnum.orElse(SortTypeEnum.DESC).getValue());
        });
        MetaData entityMetaData = commonFilter.getEntityMeta();
        MetaData relationMetaData = commonFilter.getRelationMeta();
        switch (timeFilter.getTimeFilterType()) {
            case 0:
                break;
            case 1:
                fillMetaFilter(entityFromTimeSortMap, entityFromTimeMap, entityMetaData);
                break;
            case 2:
                fillMetaFilter(entityFromTimeSortMap, entityFromTimeMap, relationMetaData);
                break;
            case 3:
                consumerIfNoNull(fromTime, commonFilter::setAttrTimeFrom);
                consumerIfNoNull(toTime, commonFilter::setAttrTimeTo);
                fillMetaFilter(entityFromTimeSortMap, entityFromTimeMap, entityMetaData);
                fillMetaFilter(entityFromTimeSortMap, entityFromTimeMap, relationMetaData);
                break;
            default:
        }
    }

    private static void fillMetaFilter(Map<Integer, Integer> fromTimeSortMap, Map<Integer, Object> fromTimeMap, MetaData metaData) {
        consumerIfNoNull(fromTimeSortMap, metaData::setSort);
        consumerIfNoNull(fromTimeMap, metaData::setFilter);
    }

}
