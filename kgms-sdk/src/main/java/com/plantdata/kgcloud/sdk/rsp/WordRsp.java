package com.plantdata.kgcloud.sdk.rsp;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-05 19:05
 **/
@Data
public class WordRsp {
    private String id;

    private String name;

    private List<String> syns;

    private String nature;
}
