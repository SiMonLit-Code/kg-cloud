package com.plantdata.kgcloud.sdk.rsp;

import lombok.Data;

import java.util.Map;

/**
 * @author cjw
 * @date 2020/4/14  16:22
 */
@Data
public class DwTableDataStatisticRsp {

    private Map<String, String> group;
    private Map<String, String> statisticResult;
}
