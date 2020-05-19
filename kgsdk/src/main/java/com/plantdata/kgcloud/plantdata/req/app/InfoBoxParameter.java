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
    @NotNull
    private Long id;
    private boolean relationAtts;
    private List<Integer> allowAtts;
    private List<String> allowAttsKey;


}
