package ai.plantdata.kgcloud.plantdata.req.rule;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/25 15:15
 */
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class InitStatisticalBeanUpdate extends InitStatisticalBeanAdd {

    @NonNull
    private Long id;
}
