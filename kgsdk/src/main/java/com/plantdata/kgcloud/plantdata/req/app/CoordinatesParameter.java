package com.plantdata.kgcloud.plantdata.req.app;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class CoordinatesParameter {
    @NotBlank
    private String kgName;
    @NotNull
    private Long azkId;
    @NotBlank
    private String type;
    private Integer size = 100;
}
