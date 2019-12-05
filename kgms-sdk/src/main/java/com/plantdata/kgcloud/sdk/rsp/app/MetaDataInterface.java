package com.plantdata.kgcloud.sdk.rsp.app;

import com.plantdata.kgcloud.sdk.rsp.app.explore.StyleRsp;
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
     * @param startTime 。
     */
    default void setStartTime(Date startTime) {
    }

    /**
     * 结束时间
     * @param endTime 。
     */
    default void setEndTime(Date endTime) {
    }

    /**
     * gis开启
     * @param openGis 。。。
     */
    default void setOpenGis(Boolean openGis){
    }

    /**
     * 经度
     * @param lng 。
     */
    default void setLng(Double lng){
    }

    /**
     * 纬度
     * @param lat 。
     */
    default void setLat(Double lat){
    }

    /**
     * 地址
     * @param address 。
     */
    default void  setAddress(String address){}

    /**
     * 样式
     *  @param  additional .
     */
    default void setAdditional(Map<String,Object> additional){

    }

    /**
     * 标签
     * @param tagRspList .
     */
    default void  setTags(List<TagRsp> tagRspList){

    }

    /**
     * 标签
     * @param style .
     */
    default void  setStyle(StyleRsp style){

    }
}
