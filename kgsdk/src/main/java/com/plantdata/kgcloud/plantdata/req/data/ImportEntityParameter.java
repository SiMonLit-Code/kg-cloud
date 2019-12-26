package com.plantdata.kgcloud.plantdata.req.data;

import com.plantdata.kgcloud.plantdata.req.entity.ImportEntityBean;
import com.plantdata.kgcloud.plantdata.validator.ChooseCheck;
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
public class ImportEntityParameter {

    @NotBlank
    private String kgName;
    private List<ImportEntityBean> data;
    private Boolean upsert = true;
    @ChooseCheck(value = "[0,1]", name = "mode")
    private Integer mode = 0;

}
