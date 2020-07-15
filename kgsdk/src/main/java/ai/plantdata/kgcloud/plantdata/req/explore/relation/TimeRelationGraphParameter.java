package ai.plantdata.kgcloud.plantdata.req.explore.relation;

import ai.plantdata.kgcloud.plantdata.constant.SortEnum;
import ai.plantdata.kgcloud.plantdata.req.explore.function.TimeGraphParameter;
import lombok.Data;

/**
 * 普通图探索类
 *
 * @author Administrator
 */
@Data
public class TimeRelationGraphParameter extends RelationGraphParameter implements TimeGraphParameter {

    private String fromTime;
    private String toTime;
    private SortEnum sort = SortEnum.DESC;
    private Integer timeFilterType = 0;


}
