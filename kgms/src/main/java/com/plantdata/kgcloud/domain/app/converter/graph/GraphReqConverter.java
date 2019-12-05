package com.plantdata.kgcloud.domain.app.converter.graph;

import ai.plantdata.kg.api.pub.req.CommonFilter;
import ai.plantdata.kg.api.pub.req.GraphFrom;
import ai.plantdata.kg.api.pub.req.MetaData;
import ai.plantdata.kg.api.pub.req.PathFrom;
import ai.plantdata.kg.api.pub.req.RelationFrom;
import com.plantdata.kgcloud.constant.MetaDataInfo;
import com.plantdata.kgcloud.sdk.req.app.TimeFilterExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.CommonReasoningExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicGraphExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.CommonExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.CommonTimingExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.PathReasoningAnalysisReq;
import com.plantdata.kgcloud.sdk.req.app.explore.PathAnalysisReq;
import com.plantdata.kgcloud.sdk.req.app.explore.PathTimingAnalysisReq;
import com.plantdata.kgcloud.sdk.req.app.explore.RelationAnalysisReq;
import com.plantdata.kgcloud.sdk.req.app.explore.RelationReasoningAnalysisReq;
import com.plantdata.kgcloud.sdk.req.app.explore.RelationTimingAnalysisReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.CommonFiltersReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.CommonPathReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.CommonRelationReq;
import com.plantdata.kgcloud.util.DateUtils;
import com.plantdata.kgcloud.util.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cjw
 * todo 优化点：使用interface提取重复代码/降低可读性
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
        if (exploreReq instanceof CommonExploreReq) {
            return commonExploreReqToGraphFrom(graphFrom, (CommonExploreReq) exploreReq);
        }
        if (exploreReq instanceof CommonTimingExploreReq) {
            return timingGraphExploreReqToGraphFrom(graphFrom, (CommonTimingExploreReq) exploreReq);
        }
        if (exploreReq instanceof CommonReasoningExploreReq) {
            return commonReasoningExploreReqToGraphFrom(graphFrom, (CommonReasoningExploreReq) exploreReq);
        }
        log.error("commonReqProxy:{}", JacksonUtils.writeValueAsString(exploreReq));
        return null;
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
        if (exploreReq instanceof PathAnalysisReq) {
            return pathAnalysisReqToPathFrom(pathFrom, (PathAnalysisReq) exploreReq);
        }
        if (exploreReq instanceof PathReasoningAnalysisReq) {
            return pathAnalysisReasonReqToPathFrom(pathFrom, (PathReasoningAnalysisReq) exploreReq);
        }
        if (exploreReq instanceof PathTimingAnalysisReq) {
            return pathTimingAnalysisReqToPathFrom(pathFrom, (PathTimingAnalysisReq) exploreReq);
        }
        log.error("pathReqProxy:{}", JacksonUtils.writeValueAsString(exploreReq));
        return null;
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
        if (exploreReq instanceof RelationAnalysisReq) {
            return relationAnalysisReqToRelationFrom(relationFrom, (RelationAnalysisReq) exploreReq);
        }
        if (exploreReq instanceof RelationTimingAnalysisReq) {
            return relationTimingAnalysisReqToRelationFrom(relationFrom, (RelationTimingAnalysisReq) exploreReq);
        }
        if (exploreReq instanceof RelationReasoningAnalysisReq) {
            return relationReasoningAnalysisReqToRelationFrom(relationFrom, (RelationReasoningAnalysisReq) exploreReq);
        }
        log.error("relationReqProxy:{}", JacksonUtils.writeValueAsString(exploreReq));
        return null;
    }

    private static GraphFrom commonReasoningExploreReqToGraphFrom(GraphFrom graphFrom, CommonReasoningExploreReq exploreReq) {
        CommonFiltersReq common = exploreReq.getCommon();
        graphFrom.setId(common.getId());
        ///todo 底层支持关键词
        //exploreReq.getKw()


        return graphFrom;
    }

    /**
     * 时序图探索请求参数转换
     */
    private static GraphFrom timingGraphExploreReqToGraphFrom(GraphFrom graphFrom, CommonTimingExploreReq exploreReq) {
        CommonFiltersReq common = exploreReq.getCommon();
        graphFrom.setId(common.getId());
        ///todo 底层支持关键词
        //exploreReq.getKw()
        TimeFilterExploreReq timeFilters = exploreReq.getTimeFilters();
        if (timeFilters != null) {
            fillTimeFilters(timeFilters, graphFrom);
        }
        return graphFrom;
    }

    /**
     * 普通探索请求参数转换
     */
    private static GraphFrom commonExploreReqToGraphFrom(GraphFrom graphFrom, CommonExploreReq exploreReq) {
        CommonFiltersReq common = exploreReq.getCommon();
        graphFrom.setId(common.getId());
        //读取元数据

        ///todo 底层支持关键词
        //exploreReq.getKw()
        //exploreReq.getAttSorts();
        return graphFrom;
    }

    /**
     * 路径分析请求参数转换
     */
    private static PathFrom pathAnalysisReqToPathFrom(PathFrom pathFrom, PathAnalysisReq analysisReq) {
        CommonPathReq path = analysisReq.getPath();
        pathFrom.setStart(path.getStart());
        pathFrom.setEnd(path.getEnd());
        pathFrom.setDistance(path.getDistance());
        ///todo 底层 9
        // path.isShortest();
        return pathFrom;
    }


    private static PathFrom pathAnalysisReasonReqToPathFrom(PathFrom pathFrom, PathReasoningAnalysisReq analysisReq) {
        CommonPathReq path = analysisReq.getPath();
        pathFrom.setStart(path.getStart());
        pathFrom.setEnd(path.getEnd());
        //提供扩展
        return pathFrom;
    }

    private static PathFrom pathTimingAnalysisReqToPathFrom(PathFrom pathFrom, PathTimingAnalysisReq analysisReq) {
        CommonPathReq path = analysisReq.getPath();
        pathFrom.setStart(path.getStart());
        pathFrom.setEnd(path.getEnd());
        TimeFilterExploreReq timeFilters = analysisReq.getTimeFilters();
        if (timeFilters != null) {
            fillTimeFilters(timeFilters, pathFrom);
        }
        return pathFrom;
    }

    private static RelationFrom relationAnalysisReqToRelationFrom(RelationFrom relationFrom, RelationAnalysisReq analysisReq) {
        CommonRelationReq relation = analysisReq.getRelation();
        relationFrom.setIds(relation.getIds());
        relationFrom.setDistance(relation.getDistance());
        //提供扩展
        return relationFrom;
    }

    private static RelationFrom relationTimingAnalysisReqToRelationFrom(RelationFrom relationFrom, RelationTimingAnalysisReq analysisReq) {
        CommonRelationReq relation = analysisReq.getRelation();
        relationFrom.setIds(relation.getIds());
        relationFrom.setDistance(relation.getDistance());

        TimeFilterExploreReq timeFilters = analysisReq.getTimeFilters();
        if (timeFilters != null) {
            fillTimeFilters(timeFilters, relationFrom);
        }
        return relationFrom;
    }

    private static RelationFrom relationReasoningAnalysisReqToRelationFrom(RelationFrom relationFrom, RelationReasoningAnalysisReq analysisReq) {
        CommonRelationReq relation = analysisReq.getRelation();
        relationFrom.setIds(relation.getIds());
        relationFrom.setDistance(relation.getDistance());
        //提供扩展
        return relationFrom;
    }

    private static <T extends CommonFilter> void fillTimeFilters(TimeFilterExploreReq timeFilter, T commonFilter) {
        //实体时间筛选
        MetaData entityMetaData = new MetaData();
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

        Map<String, Integer> entityFromTimeSortMap = new HashMap<>(1);
        entityFromTimeSortMap.put("attr_time_from", timeFilter.getSort().getValue());
        switch (timeFilter.getTimeFilterType()) {
            case 0:
                break;
            case 1:
                entityMetaData.setFilter(entityFromTimeMap);
                break;
            case 2:
                commonFilter.setAttrTimeFrom(fromTime);
                commonFilter.setAttrTimeTo(toTime);
                commonFilter.setEdgeSort(entityFromTimeSortMap);
                break;
            case 3:
                commonFilter.setAttrTimeFrom(fromTime);
                commonFilter.setAttrTimeTo(toTime);
                commonFilter.setEdgeSort(entityFromTimeSortMap);
                entityMetaData.setFilter(entityFromTimeMap);
                break;
            default:
        }
    }
}
