package ai.plantdata.kgcloud.plantdata.req.nlp;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * @author Administrator
 */
@Getter
@Setter
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
