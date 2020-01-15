package com.plantdata.kgcloud.sdk.rsp.app;

import com.plantdata.kgcloud.sdk.rsp.EntityLinkVO;
import com.plantdata.kgcloud.sdk.rsp.app.explore.OriginRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.TagRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.AdditionalRsp;
import io.swagger.annotations.ApiModelProperty;

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
    @ApiModelProperty(hidden = true)
    default void setStartTime(Date startTime) {
    }

    /**
     * 实体关联
     *
     * @param entityLinks 。
     */
    @ApiModelProperty(hidden = true)
    default void setEntityLinks(List<EntityLinkVO> entityLinks) {

    }

    /**
     * 结束时间
     *
     * @param endTime 。
     */
    @ApiModelProperty(hidden = true)
    default void setEndTime(Date endTime) {
    }

    /**
     * gis开启
     *
     * @param openGis 。。。
     */
    @ApiModelProperty(hidden = true)
    default void setOpenGis(Boolean openGis) {
    }

    /**
     * 经度
     *
     * @param lng 。
     */
    @ApiModelProperty(hidden = true)
    default void setLng(Double lng) {
    }

    /**
     * 纬度
     *
     * @param lat 。
     */
    @ApiModelProperty(hidden = true)
    default void setLat(Double lat) {
    }

    /**
     * 地址
     *
     * @param address 。
     */
    @ApiModelProperty(hidden = true)
    default void setAddress(String address) {
    }

    /**
     * 样式
     *
     * @param additional .
     */
    @ApiModelProperty(hidden = true)
    default void setAdditional(AdditionalRsp additional) {
    }

    /**
     * 节点样式
     *
     * @param nodeStyle .
     */
    @ApiModelProperty(hidden = true)
    default void setNodeStyle(Map<String, Object> nodeStyle) {
    }

    /**
     * label 样式
     *
     * @param labelStyle .
     */
    @ApiModelProperty(hidden = true)
    default void setLabelStyle(Map<String, Object> labelStyle) {
    }

    /**
     * 标签
     *
     * @param tagRspList .
     */
    @ApiModelProperty(hidden = true)
    default void setTags(List<TagRsp> tagRspList) {
    }

    /**
     * 分数
     *
     * @param score .
     */
    @ApiModelProperty(hidden = true)
    default void setScore(Double score) {
    }

    /**
     * 批次
     *
     * @param batch .
     */
    @ApiModelProperty(hidden = true)
    default void setBatch(String batch) {
    }

    /**
     * ？
     *
     * @param reliability .
     */
    @ApiModelProperty(hidden = true)
    default void setReliability(Double reliability) {
    }

    /**
     * 来源
     * @param origin
     */
    @ApiModelProperty(hidden = true)
    default void setOrigin(OriginRsp origin) {
    }
}
