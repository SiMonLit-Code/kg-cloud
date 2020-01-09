package com.plantdata.kgcloud.plantdata.req.explore.path;

import com.google.common.collect.Lists;
import com.plantdata.kgcloud.plantdata.req.explore.common.AbstrackGraphParameter;
import com.plantdata.kgcloud.plantdata.req.explore.common.GraphStatBean;
import com.plantdata.kgcloud.plantdata.req.explore.function.StatsGraphParameter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 普通图探索类
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class PathGraphParameter extends AbstrackGraphParameter implements StatsGraphParameter {
    @NotNull
    private Long start;
    @NotNull
    private Long end;
    @Max(value = 10, message = "步长不可超过10步")
    @Min(value = 1, message = "步长最小为1")
    private Integer distance = 1;
    private Boolean isShortest = false;
    private List<GraphStatBean> statsConfig;


}
