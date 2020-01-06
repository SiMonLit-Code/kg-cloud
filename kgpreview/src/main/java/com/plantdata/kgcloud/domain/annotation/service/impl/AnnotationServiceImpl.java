package com.plantdata.kgcloud.domain.annotation.service.impl;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.bean.BasePage;
import com.plantdata.kgcloud.common.util.KgQueryUtil;
import com.plantdata.kgcloud.config.AppConfig;
import com.plantdata.kgcloud.domain.annotation.entity.AnnotationRsp;
import com.plantdata.kgcloud.domain.annotation.entity.SettingReq;
import com.plantdata.kgcloud.domain.annotation.entity.TargetConcept;
import com.plantdata.kgcloud.domain.annotation.service.AnnotationService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.KgmsClient;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.plantdata.kgcloud.config.Constants.KG_MONGO;

/**
 * @author xiezhenxiang 2019/12/28
 */
@Service
public class AnnotationServiceImpl implements AnnotationService {

    @Autowired
    private KgmsClient kgmsClient;
    @Autowired
    AppConfig appConfig;

    @Override
    public BasePage<AnnotationRsp> preview(SettingReq setting) {

        checkConfig(setting);
        String kgDbName = KgQueryUtil.getKgDbName(setting.getKgName());
        List<AnnotationRsp> ls = new ArrayList<>();
        ApiReturn<BasePage<Map<String, Object>>> apiReturn = kgmsClient.dataOptFindAll(setting.getDataSetId(), 1, 20);
        for (Map<String, Object> data : apiReturn.getData().getContent()) {
            for (TargetConcept targetConcept : setting.targetConcepts) {
                Set<Long> conceptIds = KgQueryUtil.getSonConceptIds(kgDbName, true, targetConcept.conceptId);
                Bson query = Filters.and(Filters.ne("type", 0),Filters.in("concept_id", conceptIds));
                MongoCursor<Document> cursor = KG_MONGO.find(kgDbName, "basic_info", query);
                while (cursor.hasNext()) {
                    Document basic = cursor.next();
                    String name = basic.getString("name").toLowerCase();
                    double score = 0;
                    for (Map.Entry<String, Double> entry : setting.fieldsAndWeights.entrySet()) {
                        String field = entry.getKey();
                        double weight = entry.getValue();
                        String text = data.getOrDefault(field, "").toString().toLowerCase();
                        if (text.contains(name)) {
                            score += weight + (name.length() * 1.0 / text.length());
                        }
                    }
                    if (score > 0) {
                        AnnotationRsp annotationRsp = new AnnotationRsp();
                        annotationRsp.setEntityId(basic.getLong("id"));
                        annotationRsp.setEntityName(name);
                        annotationRsp.setScore(score);
                        annotationRsp.setType(basic.getInteger("type"));
                        ls.add(annotationRsp);
                        if (ls.size() >= 10) {
                            break;
                        }
                    }
                }
            }
            if (ls.size() >= 10) {
                break;
            }
        }
        return new BasePage<>(ls.size(), ls);
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
