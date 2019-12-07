package com.plantdata.kgcloud.domain.document.rsp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataCheckRsp {

    private Long conceptId;

    private String pdDocumentId;

    private String name;

    private String attName;

    private String status;

    private Long entityId;

}
