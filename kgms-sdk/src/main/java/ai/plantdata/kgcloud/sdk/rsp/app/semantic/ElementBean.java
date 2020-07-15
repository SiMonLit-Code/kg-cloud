package ai.plantdata.kgcloud.sdk.rsp.app.semantic;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Administrator
 */
@Getter
@Setter
@ApiModel("?")
public class ElementBean {
    private Long id;
    private String name;
    private int pos;
    /**
     * 0 概念 1 实体 2 属性
     */
    private int type;
    /**
     * 实体概念id
     */
    private Long classId;
    private String meaningTag;
}
