package com.plantdata.kgcloud.plantdata.req.data;

import com.plantdata.kgcloud.sdk.req.app.EntityQueryWithConditionReq;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2020/1/9 14:42
 */
@Getter
@Setter
public class EntityQueryBean {

    private List<EntityQueryWithConditionReq> names;
}
