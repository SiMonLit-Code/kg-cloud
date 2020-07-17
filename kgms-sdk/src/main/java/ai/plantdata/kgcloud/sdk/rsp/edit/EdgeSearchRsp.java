package ai.plantdata.kgcloud.sdk.rsp.edit;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/12 13:43
 */
@Getter
@Setter
public class EdgeSearchRsp {
    private String tripleId;
    private Integer attrId;
    private EdgeSearchEntityRsp fromEntity;
    private EdgeSearchEntityRsp toEntity;
    private String attrTimeFrom;
    private String attrTimeTo;
    @ApiModelProperty("权重")
    private String score = "0";
    @ApiModelProperty("来源")
    private String source;
    @ApiModelProperty("可信度")
    private String reliability = "0";
    @ApiModelProperty("边附加属性")
    private Map<Integer, Object> extraInfoMap;

    @Getter
    @Setter
    public static class EdgeSearchEntityRsp {
        private Long id;
        private String name;
        private String meaningTag;
        private Long conceptId;
    }
}
