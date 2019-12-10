package com.plantdata.kgcloud.domain.j2r.service;

import com.plantdata.kgcloud.domain.j2r.entity.Setting;
import org.bson.Document;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author xiezhenxiang 2019/12/9
 */
public interface J2rService {

    Page<Document> jsonStr(Integer dataSetId, Integer index);

    boolean checkSetting(Setting configs);

    Object pathReview(String jsonStr, List<String> jsonPathLs);

    Map<String, Object> preview(String jsonId, Setting j2rSetting);
}
