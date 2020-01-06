package com.plantdata.kgcloud.plantdata.req.explore.relation;

import com.plantdata.kgcloud.plantdata.req.explore.common.AbstrackGraphParameter;
import com.plantdata.kgcloud.plantdata.req.explore.common.GraphStatBean;
import com.plantdata.kgcloud.plantdata.req.explore.function.StatsGraphParameter;
import com.plantdata.kgcloud.plantdata.validator.ListLengthCheck;
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
 * @author Administrator
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class RelationGraphParameter extends AbstrackGraphParameter implements StatsGraphParameter {

    @Max(value = 30, message = "步长不可超过30步")
    @Min(value = 1, message = "步长最小为1")
    private Integer distance = 1;
    @NotNull
    @ListLengthCheck(min = 2)
    private List<Long> ids;
    private List<GraphStatBean> statsConfig;


}
