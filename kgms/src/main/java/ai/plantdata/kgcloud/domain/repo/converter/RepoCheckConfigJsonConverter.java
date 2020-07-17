package ai.plantdata.kgcloud.domain.repo.converter;

import ai.plantdata.cloud.util.JacksonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import ai.plantdata.kgcloud.domain.app.util.JsonUtils;
import ai.plantdata.kgcloud.domain.repo.model.RepoCheckConfig;
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
