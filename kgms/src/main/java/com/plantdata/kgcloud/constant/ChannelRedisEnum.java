package com.plantdata.kgcloud.constant;

import lombok.Getter;

/**
 * @program: kg-cloud-kgms
 * @description: 数据接入redis存储key
 * @author: czj
 * @create: 2020-03-21 22:18
 **/
@Getter
public enum  ChannelRedisEnum {

    CONFIG_KEY("channel_resourceConfig"),
    KTR_KEY("channel_resourceKtr"),
    ARRANGE_KEY("channel_resourceArrange"),
    KTR_CONFIG_KEY("channel_resourceKtrConfig"),
    ERROR_RERUN("channel_resourceRerun"),
    DELETE("channel_resourceDelete"),
    ;

    private String type;

    ChannelRedisEnum(String type) {
        this.type = type;
    }

}
