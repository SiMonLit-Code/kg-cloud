package com.plantdata.kgcloud.plantdata.req.data;

import com.plantdata.kgcloud.sdk.validator.ChooseCheck;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author Administrator
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class StatEntityGroupByConceptParameter {
    @NotBlank
    private String kgName;
    private List<Long> entityIds;
    @ChooseCheck(value = "[-1,1]", name = "sort")
    private Integer sort = -1;
    private List<Long> allowTypes;
    private List<String> allowTypesKey;
    @ChooseCheck(value = "[0,1]", name = "returnType")
    private Integer returnType = 0;
    private Integer size = 10;


}
