package ai.plantdata.kgcloud.sdk.req.app.nlp;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * @author cjw 2019-11-01 13:49:25
 */
@Getter
@Setter
@ApiModel("模型配置")
public class ModelConfig {
    private Integer id;
    private String name;
    /**
     * 0 machine learning model 1 rule based model
     */
    private int type;
    private Set<String> labels;
    private String config;
}
