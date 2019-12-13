package com.plantdata.kgcloud.sdk.req.app.infobox;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/13 9:56
 */
@Getter
@Setter
public class InfoBoxReq {

    @NotNull
    private Long id;
    private Boolean relationAttrs = true;
    private List<Integer> allowAttrs;
    private List<String> allowAttrsKey;
}
