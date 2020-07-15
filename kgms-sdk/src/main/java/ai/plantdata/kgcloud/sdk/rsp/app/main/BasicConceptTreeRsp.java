package ai.plantdata.kgcloud.sdk.rsp.app.main;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/21 17:59
 */
@Getter
@Setter
@NoArgsConstructor
@ApiModel("概念树返回列表")
public class BasicConceptTreeRsp extends BasicConceptRsp {

    @ApiModelProperty("子概念")
    private List<BasicConceptTreeRsp> children;
    @ApiModelProperty("数值属性")
    private List<NumberAttr> numAttrs;
    @ApiModelProperty("对象属性")
    private List<ObjectAttr> objAttrs;

    public BasicConceptTreeRsp(Long id, String name) {
        this.children = new ArrayList<>();
        this.numAttrs = new ArrayList<>();
        this.setId(id);
        this.setName(name);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NumberAttr {
        private Integer id;
        private String name;
        private Long conceptId;
        private Integer dataType;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ObjectAttr {
        private Integer id;
        private String name;
        private Long conceptId;
        private Integer dataType;
        private Integer direction;
        private List<Long> rangeValue;
        private List<BasicConceptTreeRsp> rangeConcept;
    }
}
