package com.plantdata.kgcloud.plantdata.req.data;

import com.plantdata.kgcloud.plantdata.req.common.PageModel;
import com.plantdata.kgcloud.plantdata.req.explore.common.EntityScreeningBean;
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
public class EntityByDataAttributeParameter extends PageModel {

    @NotBlank
    private String kgName;
    private Long conceptId;
    private String conceptKey;
    private List<EntityScreeningBean> query;

}
