package ai.plantdata.kgcloud.plantdata.req.data;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author Administrator
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class InsertConceptParameter {

    @NotBlank
    private String kgName;
    @NotNull(message = "父概念不可为空")
    private Long parentId;
    @NotBlank
    private String name;
    @Pattern(regexp = "^[a-zA-Z_]{1,}$", message = "唯一标识只能为字母和下划线")
    private String key;
    private String meaningTag;

}
