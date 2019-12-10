package com.plantdata.kgcloud.sdk.req;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-05 16:56
 **/
@Data
public class WordReq {
    @NotNull
    private String name;

    @NotNull
    private List<String> syns;

    @NotNull
    private String nature;

}
