package ai.plantdata.kgcloud.constant;

import lombok.Getter;

/**
 * @Author: LinHo
 * @Date: 2019/11/28 15:05
 * @Description:
 */
@Getter
public enum MergeType {
    /**
     * 以主体合并
     */
    SUBJECT(1),
    /**
     * 以置信度合并
     */
    SCORE(2),
    /**
     * 以来源合并
     */
    SOURCE(3),
    ;
    private Integer type;

    MergeType(Integer type) {
        this.type = type;
    }
}
