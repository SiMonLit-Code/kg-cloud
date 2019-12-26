package com.plantdata.kgcloud.plantdata.req.data;

import lombok.*;

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
public class EntityAttrDelectParameter {
    @NotBlank
    private String kgName;
    @NotNull
    private List<Long> entityIds;
    private List<String> attributeIds;
    private List<String> attrNames;
}
