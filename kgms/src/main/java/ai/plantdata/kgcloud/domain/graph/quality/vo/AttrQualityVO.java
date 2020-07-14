package ai.plantdata.kgcloud.domain.graph.quality.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: LinHo
 * @Date: 2020/3/21 15:16
 * @Description:
 */
@Setter
@Getter
public class AttrQualityVO {

    private String attrName;

    private Long attrCount;

    private Double attrIntegrity;

    private Double attrReliability;
}
