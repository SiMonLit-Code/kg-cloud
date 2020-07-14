package ai.plantdata.kgcloud.sdk.rsp.app.semantic;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author Administrator
 */
@Setter
@Getter
@ToString
public class SemanticSegWordRsp {

    /**
     * 词内容
     */
    private String word;
    /**
     * 对应的概念ID列表
     */
    private List<Long> conceptIdList;
    /**
     * 对应的概念权重列表
     */
    private List<Double> conceptScoreList;
    /**
     * 对应的概念父概念列表
     */
    private List<Long> conceptClassIdList;
    /**
     * 对应的实体ID列表
     */
    private List<Long> entityIdList;
    /**
     * 对应的实体权重列表
     */
    private List<Double> entityScoreList;
    /**
     * 对应的实体父概念列表
     */
    private List<Long> entityClassIdList;
    /**
     * 属性名，如果词对应属性时才有
     */
    private String attributeName;
    /**
     * 属性ID列表，如果词对应属性时才有
     */
    private List<Integer> attributeIdList;
    /**
     * 属性ID 与元属性序列号组成的列表，如果词对应属性的元属性时才有
     */
    private List<int[]> attributeMetaIdList;
    /**
     * 词类型，0 表示概念，1 表示实例, 3表示属性，31表示属性的元属性，51 表示范围操作符，52表示模糊操作符，53表示最值操作符，54表示非操作符，71 表示时间，72表示货币
     */
    private int type;
    /**
     * 标准值
     */
    private String normalValue;

    private int pos;

    private Integer mode;

}
