package com.plantdata.kgcloud.plantdata.req.rule;

import com.plantdata.kgcloud.plantdata.req.common.PageModel;
import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * @author Administrator
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class ListByPageParameter extends PageModel {
    @NotBlank
    private String kgName;
}
