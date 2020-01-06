package com.plantdata.kgcloud.plantdata.req.rule;

import com.plantdata.kgcloud.plantdata.req.common.PageModel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author Administrator
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class ListByPageParameter extends PageModel {
    @NotBlank(message = "kgName不能为空")
    private String kgName;
}
