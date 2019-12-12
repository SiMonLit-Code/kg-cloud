package com.plantdata.kgcloud.sdk.rsp.app;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/12 15:09
 */
@Getter
@Setter
public class RestData<T> {
    private List<T> rsData;
    private Long rsCount;

    public RestData(List<T> rsData, Long rsCount) {
        this.rsData = rsData;
        this.rsCount = rsCount;
    }
}
