package ai.plantdata.kgcloud.plantdata.bean;

import ai.plantdata.kgcloud.plantdata.rsp.MarkObject;
import ai.plantdata.kgcloud.plantdata.rsp.schema.AttributeExtraInfoItem;
import lombok.*;

import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * @author xiaohuqi E-mail:xiaohuqi@126.com
 * @version 2014-6-11
 *
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class AttributeDefinition implements java.io.Serializable, MarkObject {
    private static final long serialVersionUID = 8347376159343324248L;

    /**
     * id
     */
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 别名
     */
    private String alias;

    /**
     * 类型
     */
    private String type;

    /**
     * 定义域
     */
    private String domain;

    /**
     * 唯一值
     */
    @Pattern(regexp = "^[a-zA-Z_]{1,}$",message = "唯一标识只能为字母和下划线")
    private String key;


    /**
     * 值域
     */
    private String range;

    /**
     * 数据类型
     */
    private int dataType;

    /**
     * 单位
     */
    private String dataUnit;

    /**
     * 是否具备唯一值
     */
    private int isFunctional;

    /**
     * 额外信息
     */
    private List<AttributeExtraInfoItem> extraInfoList;
    private int tableAlone;
    private int joinSeqNo;

    /**
     * 特定大于操作描述符
     */
    private String gtRangeOperator;

    /**
     * 特定小于操作描述符
     */
    private String ltRangeOperator;

    /**
     * 特定模糊操作描述符
     */
    private String fuzzyOperator;

    /**
     * 特定最大值操作描述符
     */
    private String gtMostOperator;

    /**
     * 特定最小值操作描述符
     */
    private String ltMostOperator;

    /**
     * 特定独立最大值操作描述符
     */
    private String gtSingleMostOperator;

    /**
     * 特定最小值操作描述符
     */
    private String ltSingleMostOperator;

    /**
     * 特定最值操作描述符的单位
     */
    private String mostOperatorUnit;

    /**
     * 编辑提示
     */
    private String editTip;

    /**
     * 显示次序
     */
    private int seqNo;

    private String additionalInfo;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 最后修改人
     */
    private String modifier;

    /**
     * 最后修改时间
     */
    private String modifyTime;
    private String status;

}
