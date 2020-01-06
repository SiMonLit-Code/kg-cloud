package com.plantdata.kgcloud.sdk.req.app.dataset;

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
public class ReadTableReq extends BaseTableReq {

    @NotBlank
    private String database;
    private String table;

}
