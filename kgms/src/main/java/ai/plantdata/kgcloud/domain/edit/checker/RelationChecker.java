package ai.plantdata.kgcloud.domain.edit.checker;

import ai.plantdata.cloud.exception.BizException;
import ai.plantdata.kg.common.bean.AttributeDefinition;
import ai.plantdata.kg.common.bean.ExtraInfo;
import ai.plantdata.kgcloud.constant.DataTypeEnum;
import ai.plantdata.kgcloud.constant.MetaDataInfo;
import ai.plantdata.kgcloud.domain.app.converter.BasicConverter;
import ai.plantdata.kgcloud.sdk.rsp.edit.BatchRelationRsp;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author cjw
 * @date 2020/7/24  16:44
 */
public class RelationChecker extends BaseAttributeChecker{

    private final List<BatchRelationRsp> rspList;

    public RelationChecker(List<BatchRelationRsp> rspList, Function<List<Integer>, List<AttributeDefinition>> searchAttribute) {
        super(searchAttribute);
        this.rspList = rspList;

    }

    @Override
    public void check() {
        List<Integer> relAttrIds = BasicConverter.listToRsp(rspList, BatchRelationRsp::getAttrId);
        List<AttributeDefinition> attrDefList = searchAttribute.apply(relAttrIds);
        Map<Integer, AttributeDefinition> attrDefMap = BasicConverter.list2Map(attrDefList, AttributeDefinition::getId);

        rspList.forEach(a -> {
            AttributeDefinition def = attrDefMap.get(a.getAttrId());
            //边属性
            if (!CollectionUtils.isEmpty(a.getExtraInfoMap())) {
                Map<Integer, ExtraInfo> extraInfoMap = BasicConverter.list2Map(def.getExtraInfo(), ExtraInfo::getSeqNo);
                if (check(a.getExtraInfoMap(), extraInfoMap)) {
                    throw new BizException(403, "文本类型边属性长度不得超过50");
                }
            }
            //metadata
            if (!CollectionUtils.isEmpty(a.getMetaData())) {
                if (textTest.test(a.getMetaData().get(MetaDataInfo.SOURCE.getFieldName()))) {
                    throw new BizException(403, "来源长度不得超过50");
                }
                if (textTest.test(a.getMetaData().get(MetaDataInfo.SOURCE_ACTION.getFieldName()))) {
                    throw new BizException(403, "入图方式不得超过50");
                }
            }
        });
    }

    private boolean check(Map<Integer, Object> extraValMap, Map<Integer, ExtraInfo> extraInfoMap) {
        return extraValMap.entrySet().stream().allMatch(a -> {
            ExtraInfo extraInfo = extraInfoMap.get(a.getKey());
            return !Objects.equals(extraInfo.getDataType(), DataTypeEnum.STRING.getType()) && textTest.test(a.getValue());
        });
    }

}
