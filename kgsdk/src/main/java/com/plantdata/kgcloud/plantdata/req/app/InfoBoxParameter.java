package com.plantdata.kgcloud.plantdata.req.app;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class InfoBoxParameter {
    @NotBlank
    private String kgName;
    private Long id;
    private String kw;
    private boolean isRelationAtts;
    private List<Integer> allowAtts;
    private List<String> allowAttsKey;


}
