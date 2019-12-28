package com.plantdata.kgcloud.domain.annotation.service.impl;

import com.plantdata.kgcloud.bean.BasePage;
import com.plantdata.kgcloud.domain.annotation.entity.AnnotationRsp;
import com.plantdata.kgcloud.domain.annotation.entity.SettingReq;
import com.plantdata.kgcloud.domain.annotation.service.AnnotationService;
import com.plantdata.kgcloud.exception.BizException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author xiezhenxiang 2019/12/28
 */
@Service
public class AnnotationServiceImpl implements AnnotationService {

    @Override
    public BasePage<AnnotationRsp> preview(SettingReq setting) {

        checkConfig(setting);

        return null;
    }

    /**
     * 参数验证
     * @author xiezhenxiang 2019/6/4
     **/
    private static void checkConfig(SettingReq setting) {

        if (StringUtils.isBlank(setting.getKgName())) {
            throw new BizException(50051, "kgName can not be empty!");
        } else if (setting.getDataSetId() == null) {
            throw new BizException(50052, "dataSetId can not be empty!");
        } else if (setting.getAlgorithms() == null || setting.getAlgorithms().isEmpty()) {
            throw new BizException(50053, "algorithms can not be empty!");
        } else if (setting.getFieldsAndWeights() == null || setting.getFieldsAndWeights().isEmpty()) {
            throw new BizException(50054, "fieldsAndWeights can not be empty!");
        } else if (setting.getTargetConcepts() == null || setting.getTargetConcepts().isEmpty()) {
            throw new BizException(50055, "targetConcepts can not be empty!");
        }
    }
}
