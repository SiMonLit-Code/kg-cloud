package com.plantdata.kgcloud.domain.dataset.statistic.controller.req;

import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * @author cjw
 */
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class InterfaceUploadReq {
    @NotBlank
    private String data;
    @NotBlank
    private String dataName;

}
