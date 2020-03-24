package com.plantdata.kgcloud.plantdata.req.app;

import ai.plantdata.kg.validator.group.Insert;
import com.plantdata.kgcloud.plantdata.constant.SortEnum;
import com.plantdata.kgcloud.plantdata.req.common.PageModel;
import com.plantdata.kgcloud.sdk.validator.ChooseCheck;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;


@Data
public class PromptParameter extends PageModel {
    @NotBlank
    private String kgName;

    @NotBlank
    private String kw;
    @ChooseCheck(value = "[10,01,11,100,010,110,101,011,111]", name = "type", groups = Insert.class)
    private Integer type = 11;
    /**
     * 兼容obj Integer
     */
    private List<String> allowTypes = new ArrayList<>();
    private List<String> allowTypesKey = new ArrayList<>();
    private Integer promptType = 0;
    private Boolean isInherit = false;
    private Boolean isCaseInsensitive = true;
    private Boolean isFuzzy;
    private Boolean openExportDate = true;
    @ChooseCheck(value = "[0,1,-1]", isBlank = true)
    private Integer sort = -1;


}
