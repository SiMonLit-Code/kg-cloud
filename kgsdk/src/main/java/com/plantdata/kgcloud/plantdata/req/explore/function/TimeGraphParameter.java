package com.plantdata.kgcloud.plantdata.req.explore.function;


import com.plantdata.kgcloud.plantdata.constant.SortEnum;

/**
 * @author Administrator
 */
public interface TimeGraphParameter {

    String getFromTime();

    void setFromTime(String fromTime);

    String getToTime();

    void setToTime(String toTime);

    SortEnum getSort();

    Integer getTimeFilterType();

    void setSort(SortEnum sort);
}
