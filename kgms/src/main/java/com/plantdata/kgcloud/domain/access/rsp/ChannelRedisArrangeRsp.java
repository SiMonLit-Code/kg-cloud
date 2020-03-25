package com.plantdata.kgcloud.domain.access.rsp;

import lombok.Data;

import java.util.List;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-03-21 22:24
 **/
@Data
public class ChannelRedisArrangeRsp {

    private String resourceName;

    private String resourceType;

    private List<String> outputs;

    private List<String> distributeOriginalData;


}
