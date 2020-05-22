package com.plantdata.kgcloud.domain.repo.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.plantdata.kgcloud.domain.app.util.JsonUtils;
import com.plantdata.kgcloud.domain.repo.model.RepoCheckConfig;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.springframework.util.StringUtils;

import javax.persistence.AttributeConverter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author cjw
 * @date 2020/5/19  15:21
 */
public class RepoCheckConfigJsonConverter implements AttributeConverter<List<RepoCheckConfig>, String> {

    @Override
    public String convertToDatabaseColumn(List<RepoCheckConfig> checkConfig) {
        if (checkConfig == null) {
            return JsonUtils.objToJson(new ArrayList<>());
        }
        return JsonUtils.objToJson(checkConfig);
    }

    @Override
    public List<RepoCheckConfig> convertToEntityAttribute(String s) {
        if (!StringUtils.hasText(s)) {
            return Collections.emptyList();
        }
        return JacksonUtils.readValue(s, new TypeReference<List<RepoCheckConfig>>() {
        });
    }
}
