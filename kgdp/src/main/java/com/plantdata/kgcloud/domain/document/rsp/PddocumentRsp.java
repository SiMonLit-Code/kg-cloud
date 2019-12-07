package com.plantdata.kgcloud.domain.document.rsp;

import com.hiekn.pddocument.bean.PdDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PddocumentRsp {

    private String pddocumentId;

    private PdDocument pdDocument;

}
