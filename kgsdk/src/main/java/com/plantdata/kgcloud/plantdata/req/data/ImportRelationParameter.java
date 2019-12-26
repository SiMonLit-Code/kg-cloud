package com.plantdata.kgcloud.plantdata.req.data;

import com.plantdata.kgcloud.plantdata.bean.ImportRelationBean;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class ImportRelationParameter {

    @NotBlank
    private String kgName;
    private Boolean entityUpsert = false;
    private List<ImportRelationBean> data;

}
