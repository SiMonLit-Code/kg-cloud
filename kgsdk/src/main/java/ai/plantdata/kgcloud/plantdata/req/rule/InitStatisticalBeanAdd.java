package ai.plantdata.kgcloud.plantdata.req.rule;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @author Administrator
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class InitStatisticalBeanAdd {

    @NotBlank
    private String kgName;
    @NotBlank
    private String type;
    @NotNull
    private Map<String,Object> rule;

}