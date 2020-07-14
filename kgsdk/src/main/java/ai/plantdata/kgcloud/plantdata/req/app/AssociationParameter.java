package ai.plantdata.kgcloud.plantdata.req.app;

import ai.plantdata.kgcloud.sdk.validator.ChooseCheck;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author Administrator
 */
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class AssociationParameter {
    @NotBlank
    private String kgName;
    private Long entityId;
    private String kw;
    @ChooseCheck(value = "[0,1,2]")
    private Integer direction = 1;

    private List<Integer> allowAtts;
    private List<String> allowAttsKey;
    @Min(0)
    private Integer pageSize = 10;

}
