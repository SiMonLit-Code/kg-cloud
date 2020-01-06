package com.plantdata.kgcloud.plantdata.req.rule;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Administrator
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class GraphBusinessAlgorithmAdd {
    @NotBlank
    private String kgName;
    @NotNull
    private GraphBusinessAlgorithmBean bean;
}
