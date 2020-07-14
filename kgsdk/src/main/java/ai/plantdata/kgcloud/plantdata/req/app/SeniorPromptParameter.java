package ai.plantdata.kgcloud.plantdata.req.app;

import ai.plantdata.kgcloud.plantdata.req.common.PageModel;
import ai.plantdata.kgcloud.plantdata.req.explore.common.EntityScreeningBean;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author Administrator
 */
@Data
public class SeniorPromptParameter extends PageModel {
    @NotBlank
    private String kgName;
    private Long conceptId;
    private String conceptKey;

    private String kw;

    private Boolean isFuzzy;

    private Boolean openExportDate = true;

    private List<EntityScreeningBean> query;

}
