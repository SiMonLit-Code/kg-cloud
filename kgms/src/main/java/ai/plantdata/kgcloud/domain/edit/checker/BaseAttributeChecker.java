package ai.plantdata.kgcloud.domain.edit.checker;

import ai.plantdata.cloud.exception.BizException;
import ai.plantdata.kg.common.bean.AttributeDefinition;
import ai.plantdata.kgcloud.constant.MetaDataInfo;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author cjw
 * @date 2020/7/27  14:49
 */
public abstract class BaseAttributeChecker {

    protected final Function<List<Integer>, List<AttributeDefinition>> searchAttribute;
    protected final Predicate<Object> textTest = a -> a != null && a.toString().length() > 50;

    public BaseAttributeChecker(Function<List<Integer>, List<AttributeDefinition>> searchAttribute) {
        this.searchAttribute = searchAttribute;
    }

    /**
     * 检测
     */
    abstract void check();


    protected void metaDataCheck(Map<String,Object> m){
        if(CollectionUtils.isEmpty(m)){
            return;
        }
        if (textTest.test(m.get(MetaDataInfo.SOURCE.getFieldName()))) {
            throw new BizException(403, "来源长度不得超过50");
        }
        if (textTest.test(m.get(MetaDataInfo.SOURCE_ACTION.getFieldName()))) {
            throw new BizException(403, "入图方式不得超过50");
        }
    }
}
