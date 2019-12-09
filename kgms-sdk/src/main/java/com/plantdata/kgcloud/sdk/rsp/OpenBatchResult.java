package com.plantdata.kgcloud.sdk.rsp;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/9 11:26
 */
@Getter
@Setter
public class OpenBatchResult<T> {

    private List<T> success;
    private List<T> error;
}
