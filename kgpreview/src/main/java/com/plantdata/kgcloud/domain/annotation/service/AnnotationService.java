package com.plantdata.kgcloud.domain.annotation.service;

import com.plantdata.kgcloud.bean.BasePage;
import com.plantdata.kgcloud.domain.annotation.entity.AnnotationRsp;
import com.plantdata.kgcloud.domain.annotation.entity.SettingReq;

/**
 * @author xiezhenxiang 2019/12/28
 */
public interface AnnotationService {

    BasePage<AnnotationRsp> preview(SettingReq setting);
}
