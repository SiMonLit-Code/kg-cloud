package ai.plantdata.kgcloud.plantdata.req.reason;

import com.alibaba.fastjson.JSONObject;
import ai.plantdata.kgcloud.sdk.constant.CubeTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Administrator
 */
@Getter
@Setter
public class RuleCube {
    private String id;
    private CubeTypeEnum type;
    private List<FilterBean> filterList;
    private List<FilterBean> aggregateFilterList;
    private Long conceptId;
    private Integer attrId;
    private int direction;
    private int output;
    private LoopBean loop;
    private JSONObject statParam;

}
