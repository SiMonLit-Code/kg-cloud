package ai.plantdata.kgcloud.plantdata.req.explore;

import ai.plantdata.kgcloud.plantdata.constant.SortEnum;
import ai.plantdata.kgcloud.plantdata.req.explore.common.GeneralGraphParameter;
import ai.plantdata.kgcloud.plantdata.req.explore.function.TimeGraphParameter;
import ai.plantdata.kgcloud.plantdata.req.common.AttrSortBean;
import ai.plantdata.kgcloud.sdk.validator.ChooseCheck;
import ai.plantdata.kgcloud.plantdata.validator.DateCheck;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 普通图探索类
 *
 * @author Administrator
 */
@Data
public class TimeGeneralGraphParameter extends GeneralGraphParameter implements TimeGraphParameter {
    @DateCheck(name = "fromTime")
    private String fromTime;
    @DateCheck(name = "toTime")
    private String toTime;

    private SortEnum sort = SortEnum.DESC;

    @ChooseCheck(value = "[0,1,2,3]", name = "timeFilterType")
    private Integer timeFilterType = 0;

    @Override
    public Integer getHyponymyDistance() {
        return 0;
    }

    @Override
    public List<AttrSortBean> getAttSorts() {
        return new ArrayList<>();
    }


}
