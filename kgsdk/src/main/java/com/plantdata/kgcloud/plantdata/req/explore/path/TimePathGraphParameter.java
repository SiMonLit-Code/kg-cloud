package com.plantdata.kgcloud.plantdata.req.explore.path;

import com.plantdata.kgcloud.plantdata.constant.SortEnum;
import com.plantdata.kgcloud.plantdata.req.explore.function.TimeGraphParameter;
import lombok.Data;

/**
 * 普通图探索类
 */
@Data
public class TimePathGraphParameter extends PathGraphParameter implements TimeGraphParameter {

    private  String fromTime;
    private String toTime;
    private SortEnum sort = SortEnum.DESC;
    private Integer timeFilterType = 0;

}
