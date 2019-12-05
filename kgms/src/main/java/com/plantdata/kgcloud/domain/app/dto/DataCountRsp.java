package com.plantdata.kgcloud.domain.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/5 13:06
 */
@Getter
@NoArgsConstructor
public class DataCountRsp<T> {
    private List<T> rsData;
    private Long rsCount;

    public DataCountRsp(List<T> rsData, Long rsCount) {
        this.rsData = rsData;
        this.rsCount = rsCount;
    }
}
