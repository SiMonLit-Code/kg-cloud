package com.plantdata.kgcloud.plantdata.req.dataset;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class InterfaceUploadParameter {
    @NotBlank
    private String data;
    @NotBlank
    private String dataName;

}
