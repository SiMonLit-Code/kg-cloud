package com.plantdata.kgcloud.plantdata.converter.common;

import com.plantdata.kgcloud.plantdata.req.explore.EntityScreeningBean;
import com.plantdata.kgcloud.sdk.req.app.EntityQueryFiltersReq;
import lombok.NonNull;
import org.springframework.beans.BeanUtils;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/23 13:52
 */
public class MongoQueryConverter extends BasicConverter {

    public static EntityQueryFiltersReq entityScreeningBeanToEntityQueryFiltersReq(@NonNull EntityScreeningBean screeningBean) {
        EntityQueryFiltersReq entityQueryFiltersReq = new EntityQueryFiltersReq();
        BeanUtils.copyProperties(screeningBean, entityQueryFiltersReq);
        consumerIfNoNull(screeningBean.get$ne(), entityQueryFiltersReq::set$neq);
        return entityQueryFiltersReq;
    }
}
