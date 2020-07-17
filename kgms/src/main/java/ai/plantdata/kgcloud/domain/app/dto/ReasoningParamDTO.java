package ai.plantdata.kgcloud.domain.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/4 14:39
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReasoningParamDTO {
    private List<Long> analysisEntityIds;
    private Integer distance;
}
