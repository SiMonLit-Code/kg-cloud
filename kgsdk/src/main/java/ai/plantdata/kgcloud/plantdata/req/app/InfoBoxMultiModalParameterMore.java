package ai.plantdata.kgcloud.plantdata.req.app;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class InfoBoxMultiModalParameterMore {
    @NotNull
    private String kgName;
    private List<Long> ids;
    private List<String> kws;

}
