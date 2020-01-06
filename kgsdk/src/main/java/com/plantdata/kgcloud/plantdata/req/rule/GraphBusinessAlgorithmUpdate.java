package com.plantdata.kgcloud.plantdata.req.rule;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Administrator
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class GraphBusinessAlgorithmUpdate {
    @NotBlank
    private String kgName;
    @NotNull
    private GraphBusinessAlgorithmBean bean;
    @NotNull
    private Long id;
}
