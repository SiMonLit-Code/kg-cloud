package com.plantdata.kgcloud.domain.common.rsp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileRsp {

    private String name;
    private String type;
    private long size;
    private String source;
}
