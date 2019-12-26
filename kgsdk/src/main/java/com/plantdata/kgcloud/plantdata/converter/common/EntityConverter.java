package com.plantdata.kgcloud.plantdata.converter.common;

import com.plantdata.kgcloud.plantdata.req.common.Additional;
import com.plantdata.kgcloud.plantdata.req.entity.EntityBean;
import com.plantdata.kgcloud.sdk.rsp.app.explore.BasicEntityRsp;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/26 16:16
 */
public class EntityConverter {

    public static <T extends BasicEntityRsp, R extends EntityBean> R basicEntityRspToEntityBean(T newEntity, R oldEntity) {
        oldEntity.setId(newEntity.getId());
        oldEntity.setName(newEntity.getName());
        oldEntity.setMeaningTag(newEntity.getMeaningTag());
        oldEntity.setConceptId(newEntity.getConceptId());
        oldEntity.setCreationTime(newEntity.getCreationTime());
        oldEntity.setType(newEntity.getType());
        Additional additional = new Additional();
        oldEntity.setAdditionalInfo(additional);
        return oldEntity;
    }

}
