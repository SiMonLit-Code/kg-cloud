package com.plantdata.kgcloud.domain.data.req;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @Author: LinHo
 * @Date: 2020/3/24 16:40
 * @Description:
 */
@Getter
@Setter
@ApiModel("修正数据")
public class DataStoreModifyReq {

    private String id;

    private Map<String, Object> data;
}
