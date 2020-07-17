package ai.plantdata.kgcloud.plantdata.req.data;

import ai.plantdata.kgcloud.plantdata.req.entity.ImportEntityBean;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Administrator
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class EntityInsertParameter {

    @NotBlank
    private String kgName;

    @NotNull
    private List<ImportEntityBean> data;

}
