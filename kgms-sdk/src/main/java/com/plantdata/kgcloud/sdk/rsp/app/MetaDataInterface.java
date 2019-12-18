package com.plantdata.kgcloud.sdk.rsp.app;

import com.plantdata.kgcloud.sdk.rsp.EntityLinkVO;
import com.plantdata.kgcloud.sdk.rsp.app.explore.TagRsp;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/2 11:24
 */
public interface MetaDataInterface {
    /**
     * 开始时间
     *
     * @param startTime 。
     */
    default void setStartTime(Date startTime) {
    }

    /**
     * 实体关联
     *
     * @param entityLinks 。
     */
    default void setEntityLinks(List<EntityLinkVO> entityLinks) {

    }

    /**
     * 结束时间
     *
     * @param endTime 。
     */
    default void setEndTime(Date endTime) {
    }

    /**
     * gis开启
     *
     * @param openGis 。。。
     */
    default void setOpenGis(Boolean openGis) {
    }

    /**
     * 经度
     *
     * @param lng 。
     */
    default void setLng(Double lng) {
    }

    /**
     * 纬度
     *
     * @param lat 。
     */
    default void setLat(Double lat) {
    }

    /**
     * 地址
     *
     * @param address 。
     */
    default void setAddress(String address) {
    }

    /**
     * 样式
     *
     * @param additional .
     */
    default void setAdditional(Map<String, Object> additional) {
    }

    default void setNodeStyle(Map<String, Object> nodeStyle) {
    }

    default void setLabelStyle(Map<String, Object> labelStyle) {
    }

    /**
     * 标签
     *
     * @param tagRspList .
     */
    default void setTags(List<TagRsp> tagRspList) {
    }

    /**
     * 分数
     *
     * @param score .
     */
    default void setScore(Double score) {
    }

    /**
     * 批次
     *
     * @param batch .
     */
    default void setBatch(String batch) {
    }

    default void setReliability(Double reliability) {
    }
}
