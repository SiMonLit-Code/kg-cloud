package com.plantdata.kgcloud.sdk.rsp.app.explore;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author cjw
 * @version 1.0
 * @date 2020/1/2 20:26
 */
@Getter
@Setter
@NoArgsConstructor
public class FileRsp {

    private String name;
    private String href;
    private String type;
}
