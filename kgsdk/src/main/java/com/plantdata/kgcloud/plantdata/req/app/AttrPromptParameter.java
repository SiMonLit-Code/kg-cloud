package com.plantdata.kgcloud.plantdata.req.app;

import com.plantdata.kgcloud.plantdata.req.common.PageModel;
import com.plantdata.kgcloud.sdk.validator.ChooseCheck;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author Administrator
 */
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class AttrPromptParameter extends PageModel {
    @NotBlank
    private String kgName;
    private Integer attrId;
    private String attrKey;
    private Integer seqNo;
    @ChooseCheck(value = "[1,0]", name = "isReserved")
    private Integer isReserved = 0;
    @ChooseCheck(value = "[1,2]", name = "dataType")
    private Integer dataType = 2;
    private String kw;
    private String searchOption;
    @ChooseCheck(value = "[-1,1,0]", name = "sort")
    private Integer sort = -1;
}
