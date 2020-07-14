package ai.plantdata.kgcloud.domain.app.dto;

import ai.plantdata.kg.api.pub.resp.GraphVO;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/5 13:02
 */
@Getter
@Setter
@NoArgsConstructor
public class GraphReasoningDTO extends GraphVO {
    private Map<Integer, Long> ruleIdMap;

    public GraphReasoningDTO(Map<Integer, Long> ruleIdMap) {
        this.ruleIdMap = ruleIdMap;
        this.setRelationList(Lists.newArrayList());
        this.setEntityList(Lists.newArrayList());
    }
}
