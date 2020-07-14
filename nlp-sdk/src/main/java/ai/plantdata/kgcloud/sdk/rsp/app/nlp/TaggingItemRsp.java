package ai.plantdata.kgcloud.sdk.rsp.app.nlp;

import lombok.Getter;
import lombok.Setter;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/6 18:31
 */
@Getter
@Setter
public class TaggingItemRsp {

    private Long id;
    private String name;
    private Double score;
    private Long classId;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getScore() {
        return score;
    }

    public Long getClassId() {
        return classId;
    }
}
