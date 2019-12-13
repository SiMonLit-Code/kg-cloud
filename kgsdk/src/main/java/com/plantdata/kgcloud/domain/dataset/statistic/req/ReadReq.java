package com.plantdata.kgcloud.domain.dataset.statistic.req;
import com.plantdata.kgcloud.sdk.req.app.dataset.ReadTableReq;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author cjw 2019-11-07 14:28:46
 */
@Getter
@Setter
public class ReadReq extends ReadTableReq {

    @NotBlank
    private String dataName;

}
