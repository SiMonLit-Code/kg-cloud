package com.plantdata.kgcloud.plantdata.req.rule;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class GraphBusinessAlgorithmRequestAdd {
    @NotBlank
    private String kgName;
    @NotNull
    private GraphBusinessAlgorithmBean bean;
}
