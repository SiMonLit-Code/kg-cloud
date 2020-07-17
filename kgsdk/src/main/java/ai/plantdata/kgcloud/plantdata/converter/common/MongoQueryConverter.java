package ai.plantdata.kgcloud.plantdata.converter.common;

import ai.plantdata.cloud.util.JacksonUtils;
import ai.plantdata.kgcloud.plantdata.req.explore.common.EntityScreeningBean;
import ai.plantdata.kgcloud.util.JsonUtils;
import ai.plantdata.kgcloud.sdk.req.app.DataAttrReq;
import ai.plantdata.kgcloud.sdk.req.app.EntityQueryFiltersReq;
import lombok.NonNull;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/23 13:52
 */
public class MongoQueryConverter extends BasicConverter {

    public static EntityQueryFiltersReq entityScreeningBeanToEntityQueryFiltersReq(@NonNull EntityScreeningBean screeningBean) {
        if (screeningBean == null) {
            return null;
        }
        EntityQueryFiltersReq entityQueryFiltersReq = JsonUtils.parseObj(JacksonUtils.writeValueAsString(screeningBean), EntityQueryFiltersReq.class);
        consumerIfNoNull(entityQueryFiltersReq, a -> {
            consumerIfNoNull(screeningBean.get$ne(), a::set$ne);
            a.setAttrDefId(screeningBean.getAttrId());
        });
        return entityQueryFiltersReq;
    }

    public static DataAttrReq entityScreeningBeanToEntityDataAttrReq(@NonNull EntityScreeningBean screeningBean){
        if (screeningBean == null) {
            return null;
        }
        DataAttrReq dataAttrReq = JsonUtils.parseObj(JacksonUtils.writeValueAsString(screeningBean), DataAttrReq.class);
        consumerIfNoNull(dataAttrReq, a -> {
            consumerIfNoNull(screeningBean.get$ne(), a::set$ne);
            a.setAttrDefId(screeningBean.getAttrId());
        });
        return dataAttrReq;
    }
}
