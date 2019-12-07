package com.plantdata.kgcloud.domain.document.rsp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConceptRsp {

    private Integer id;

    private Integer documentId;

    private String Name;

    private String oldName;

    private Boolean isImportGraph;

    private Long conceptId;

    private String conceptStatus;

    private List<AttrRsp> attrs;

}
