package ai.plantdata.kgcloud.domain.edit.rsp;

import ai.plantdata.kg.api.edit.merge.AttributeItem;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class MergeEntityDetailRsp {

    /**
     * 唯一ID
     */
    private long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型：0表示概念，1表示实例
     */
    private int type;

    /**
     * 摘要
     */
    private String meaningTag;

    /**
     * 图片url
     */
    private String imageUrl;

    /**
     * 摘要
     */
    private String abs;

    /**
     * 属性列表
     */
    private List<AttributeItem> atts;

    private Map<String, Object> metaData;

    /**
     * 可信度
     */
    private String reliability;

    /**
     * 来源
     */
    private String source;

}
