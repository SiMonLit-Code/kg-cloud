package com.plantdata.kgcloud.plantdata.req.data;

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
public class AttributeParameter {
    @NotBlank
    private String kgName;
    private Long conceptId;
    private String conceptKey;
    private Boolean inherit = true;
}
