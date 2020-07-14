package ai.plantdata.kgcloud.sdk.rsp.app.main;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/11 14:24
 */
@Getter
@Setter
@ApiModel("知识卡片父子概念视图")
public class InfoBoxConceptRsp {

    private Long id;
    private String name;
    private String meaningTag;
    private String imageUrl;
}
