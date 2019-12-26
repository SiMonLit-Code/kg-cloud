package com.plantdata.kgcloud.plantdata.req.data;


import com.plantdata.kgcloud.plantdata.bean.ImportRelationBean;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class UpdataRelationParameter {
    @NotBlank
    private String kgName;

    @NotNull
    private List<ImportRelationBean> data;

    private Integer mode = 0;

}
