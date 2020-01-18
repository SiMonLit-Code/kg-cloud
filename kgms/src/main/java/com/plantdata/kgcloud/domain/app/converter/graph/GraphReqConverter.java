package com.plantdata.kgcloud.domain.app.converter.graph;

import ai.plantdata.kg.api.pub.req.CommonFilter;
import ai.plantdata.kg.api.pub.req.GraphFrom;
import ai.plantdata.kg.api.pub.req.MetaData;
import ai.plantdata.kg.api.pub.req.PathFrom;
import ai.plantdata.kg.api.pub.req.RelationFrom;
import com.google.common.collect.Maps;
import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.constant.AppErrorCodeEnum;
import com.plantdata.kgcloud.constant.MetaDataInfo;
import com.plantdata.kgcloud.constant.TimeFilterTypeEnum;
import com.plantdata.kgcloud.domain.app.converter.BasicConverter;
import com.plantdata.kgcloud.domain.app.converter.ConditionConverter;
import com.plantdata.kgcloud.domain.app.util.JsonUtils;
import com.plantdata.kgcloud.domain.common.util.EnumUtils;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.constant.SortTypeEnum;
import com.plantdata.kgcloud.sdk.req.app.TimeFilterExploreReq;
import com.plantdata.kgcloud.sdk.req.app.dataset.PageReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicGraphExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.CommonFiltersReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.CommonPathReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.CommonRelationReq;
import com.plantdata.kgcloud.sdk.req.app.function.GraphCommonReqInterface;
import com.plantdata.kgcloud.sdk.req.app.function.GraphPathReqInterface;
import com.plantdata.kgcloud.sdk.req.app.function.GraphRelationReqInterface;
import com.plantdata.kgcloud.sdk.req.app.function.GraphTimingReqInterface;
import com.plantdata.kgcloud.util.DateUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
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
     * 图探索参数 构建 GraphFrom 支持分页
     *
     * @param exploreReq 探索的参数
     * @param <E>        。。。
     * @return 。。
     */
    public static <E extends BasicGraphExploreReq> GraphFrom commonReqProxy(E exploreReq) {
        GraphFrom graphFrom = GraphCommonConverter.basicReqToRemote(exploreReq, new GraphFrom());
        if (exploreReq instanceof GraphCommonReqInterface) {
            fillCommon(((GraphCommonReqInterface) exploreReq).fetchCommon(), exploreReq.getPage(), graphFrom);
        }
        if (exploreReq instanceof GraphTimingReqInterface) {
            fillTimeFilters(((GraphTimingReqInterface) exploreReq).fetchTimeFilter(), graphFrom);
        }
        log.info("graphFrom:{}", JsonUtils.objToJson(graphFrom));
        return graphFrom;
    }

    /**
     * 路径分析参数 构建 PathFrom 暂不支持分页
     *
     * @param exploreReq 探索的参数
     * @param <E>        。。。
     * @return 。。
     */
    public static <E extends BasicGraphExploreReq> PathFrom pathReqProxy(E exploreReq) {
        PathFrom pathFrom = GraphCommonConverter.basicReqToRemote(exploreReq, new PathFrom());
        if (exploreReq instanceof GraphPathReqInterface) {
            fillPath(((GraphPathReqInterface) exploreReq).fetchPath(), pathFrom);
        }
        if (exploreReq instanceof GraphTimingReqInterface) {
            fillTimeFilters(((GraphTimingReqInterface) exploreReq).fetchTimeFilter(), pathFrom);
        }
        log.info("pathFrom:{}", JsonUtils.objToJson(pathFrom));
        return pathFrom;
    }

    /**
     * 路径分析参数 构建 PathFrom 暂不支持分页
     *
     * @param exploreReq 探索的参数
     * @param <E>        。。。
     * @return 。。
     */
    public static <E extends BasicGraphExploreReq> RelationFrom relationReqProxy(E exploreReq) {
        RelationFrom relationFrom = GraphCommonConverter.basicReqToRemote(exploreReq, new RelationFrom());
        //关联分析
        if (exploreReq instanceof GraphRelationReqInterface) {
            fillRelation(((GraphRelationReqInterface) exploreReq).fetchRelation(), relationFrom);
        }
        if (exploreReq instanceof GraphTimingReqInterface) {
            fillTimeFilters(((GraphTimingReqInterface) exploreReq).fetchTimeFilter(), relationFrom);
        }
        log.info("relationFrom:{}", JsonUtils.objToJson(relationFrom));
        return relationFrom;
    }


    private static void fillRelation(@NonNull CommonRelationReq relation, RelationFrom graphFrom) {
        graphFrom.setIds(relation.getIds());
    }

    private static void fillCommon(@NonNull CommonFiltersReq common, PageReq page, GraphFrom graphFrom) {
        if (common.getId() == null && StringUtils.isEmpty(common.getKw())) {
            throw BizException.of(AppErrorCodeEnum.NULL_KW_AND_ID);
        }
        if (page == null) {
            page = new PageReq();
            page.setPage(NumberUtils.INTEGER_ONE);
            page.setSize(BaseReq.DEFAULT_SIZE);
        }

        //最高层节点数
        graphFrom.setSkip(NumberUtils.INTEGER_ZERO);
        graphFrom.setLimit(common.getHighLevelSize());
        //通用
        graphFrom.getHighLevelFilter().setSkip(page.getOffset());
        graphFrom.getHighLevelFilter().setLimit(page.getLimit());
        graphFrom.getHighLevelFilter().setDirection(common.getDirection());
        graphFrom.getHighLevelFilter().setQueryPrivate(common.isPrivateAttRead());
        if (!CollectionUtils.isEmpty(common.getEdgeAttrSorts())) {
            Map<String, Integer> edgeAttrQuery = ConditionConverter.relationAttrSortToMap(common.getEdgeAttrSorts());
            graphFrom.getHighLevelFilter().setEdgeSort(edgeAttrQuery);
        }
        graphFrom.setDirection(common.getDirection());
        graphFrom.setId(common.getId());
        graphFrom.setName(common.getKw());
        graphFrom.setQueryPrivate(common.isPrivateAttRead());

        consumerIfNoNull(common.getHyponymyDistance(), graphFrom::setHyponymyDistance);

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
        Optional<TimeFilterTypeEnum> filterType = EnumUtils.parseById(TimeFilterTypeEnum.class, timeFilter.getTimeFilterType());
        if (!filterType.isPresent()) {
            return;
        }
        consumerIfNoNull(filterType.get(), a -> {
            commonFilter.getHighLevelFilter().setTimeFilterType(a.fetchId());
            commonFilter.setTimeFilterType(a.fetchId());
            if (!TimeFilterTypeEnum.ENTITY.equals(a) && !TimeFilterTypeEnum.NO_FILTER.equals(a)) {
                commonFilter.setQueryPrivate(false);
                commonFilter.getHighLevelFilter().setQueryPrivate(false);
            }
        });
        if (null == timeFilter.getFromTime() && null == timeFilter.getToTime()) {
            return;
        }
        //关系时间筛选参数
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
        //实体时间筛选参数
        Map<Integer, Object> entityFromTimeMap = Maps.newHashMapWithExpectedSize(2);
        //实体时间排序参数
        Map<Integer, Integer> entityFromTimeSortMap =Maps.newHashMapWithExpectedSize(1);
        consumerIfNoNull(timeRangeMap, a -> entityFromTimeMap.put(Integer.parseInt(MetaDataInfo.FROM_TIME.getCode()), a));
        consumerIfNoNull(timeFilter.getSort(), a -> {
            Optional<SortTypeEnum> sortTypeEnum = SortTypeEnum.parseByName(timeFilter.getSort());
            entityFromTimeSortMap.put(Integer.parseInt(MetaDataInfo.FROM_TIME.getCode()), sortTypeEnum.orElse(SortTypeEnum.DESC).getValue());
        });
        MetaData entityMetaData = commonFilter.getEntityMeta();
        switch (timeFilter.getTimeFilterType()) {
            case 1:
                fillMetaFilter(entityFromTimeSortMap, entityFromTimeMap, entityMetaData);
                break;
            case 2:
                consumerIfNoNull(fromTime, commonFilter::setAttrTimeFrom);
                consumerIfNoNull(toTime, commonFilter::setAttrTimeTo);
                break;
            case 3:
                consumerIfNoNull(fromTime, commonFilter::setAttrTimeFrom);
                consumerIfNoNull(toTime, commonFilter::setAttrTimeTo);
                fillMetaFilter(entityFromTimeSortMap, entityFromTimeMap, entityMetaData);
                break;
            default:
                break;
        }
    }

    private static void fillMetaFilter(Map<Integer, Integer> fromTimeSortMap, Map<Integer, Object> fromTimeMap, MetaData metaData) {
        consumerIfNoNull(fromTimeSortMap, metaData::setSort);
        consumerIfNoNull(fromTimeMap, metaData::setFilter);
    }

}
