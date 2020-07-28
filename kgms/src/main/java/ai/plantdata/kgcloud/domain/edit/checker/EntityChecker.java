package ai.plantdata.kgcloud.domain.edit.checker;

import ai.plantdata.cloud.exception.BizException;
import ai.plantdata.kg.common.bean.AttributeDefinition;
import ai.plantdata.kgcloud.constant.DataTypeEnum;
import ai.plantdata.kgcloud.constant.MetaDataInfo;
import ai.plantdata.kgcloud.domain.app.converter.BasicConverter;
import ai.plantdata.kgcloud.sdk.rsp.app.OpenBatchSaveEntityRsp;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @date 2020/7/27  14:05
 */
public class EntityChecker extends BaseAttributeChecker {
    private final List<OpenBatchSaveEntityRsp> batchEntity;

    public EntityChecker(List<OpenBatchSaveEntityRsp> batchEntity, Function<List<Integer>, List<AttributeDefinition>> searchAttribute) {
        super(searchAttribute);
        this.batchEntity = batchEntity;
    }

    @Override
    public void check() {
        List<Integer> attrDefIds = batchEntity.stream()
                .filter(a -> !CollectionUtils.isEmpty(a.getAttributes()))
                .flatMap(a -> a.getAttributes().keySet().stream())
                .distinct()
                .collect(Collectors.toList());
        List<AttributeDefinition> attrDefList = searchAttribute.apply(attrDefIds);
        Map<Integer, AttributeDefinition> attrDefMap = BasicConverter.list2Map(attrDefList, AttributeDefinition::getId);
        batchEntity.forEach(a -> {
            //同义词
            BasicConverter.consumerIfNoNull(a.getSynonyms(), b -> {
                if (b.stream().anyMatch(textTest)) {
                    throw new BizException(403, "同义词长度不能超过50");
                }
            });
            //属性
            BasicConverter.consumerIfNoNull(a.getAttributes(), b -> {
                boolean anyMatch = b.entrySet().stream().filter(entry -> {
                    if (!attrDefMap.containsKey(entry.getKey())) {
                        return false;
                    }
                    AttributeDefinition definition = attrDefMap.get(entry.getKey());
                    return definition.getDataType().equals(DataTypeEnum.STRING.getType());
                }).anyMatch(entry -> textTest.test(entry.getValue()));
                if (anyMatch) {
                    throw new BizException(403, "短文本类型属性值长度不能超过50");
                }
            });

            BasicConverter.consumerIfNoNull(a.getPrivateAttributes(), p -> {
                p.forEach((k, v) -> {
                    if (textTest.test(k)) {
                        throw new BizException(403, "私有属性名称长度不能超过50");
                    }
                    if (textTest.test(v)) {
                        throw new BizException(403, "私有属性值长度不能超过50");
                    }
                });
            });
            //metadata
            super.metaDataCheck(a.getMetaDataMap());
            //属性定义metaData
            BasicConverter.listConsumerIfNoNull(a.getAttrValueMetaData().values(), super::metaDataCheck);
        });

    }
}
