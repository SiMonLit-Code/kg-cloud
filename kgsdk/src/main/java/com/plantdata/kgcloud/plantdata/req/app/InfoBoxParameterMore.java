package com.plantdata.kgcloud.plantdata.req.app;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Administrator
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class InfoBoxParameterMore {
    @NotNull
    private String kgName;
    private List<Long> ids;
    private Boolean isRelationAtts = true;
    private List<Integer> allowAtts;
    private List<String> allowAttsKey;

}
