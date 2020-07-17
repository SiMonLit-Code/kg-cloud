package ai.plantdata.kgcloud.plantdata.req.data;

import ai.plantdata.kgcloud.plantdata.req.entity.ImportEntityBean;
import ai.plantdata.kgcloud.sdk.validator.ChooseCheck;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author Administrator
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImportEntityParameter {

    @NotBlank
    private String kgName;
    private List<ImportEntityBean> data;
    private Boolean upsert = true;
    @ChooseCheck(value = "[0,1]", name = "mode",isBlank = true)
    private Integer mode = 0;

}
