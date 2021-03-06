package ai.plantdata.kgcloud.plantdata.req.explore.common;

import ai.plantdata.kgcloud.plantdata.req.common.AttrSortBean;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 普通图探索类
 *
 * @author Administrator
 */
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class GeneralGraphParameter extends AbstractGraphParameter {


    private Long id;


    private String kw;
    private Integer direction = 0;
    private Integer highLevelSize = 10;

    private String graphRule;


    private String graphRuleKgql;
    private Integer hyponymyDistance;

    private List<AttrSortBean> attSorts;
    private Integer pageNo = 1;
    private Integer pageSize = 10;


}
