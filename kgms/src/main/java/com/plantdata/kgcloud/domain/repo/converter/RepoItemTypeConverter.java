package com.plantdata.kgcloud.domain.repo.converter;

import com.plantdata.kgcloud.domain.repo.enums.RepoItemType;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.AttributeConverter;
import java.util.Arrays;

/**
 * @author cjw
 * @date 2020/5/19  15:21
 */
public class RepoItemTypeConverter implements AttributeConverter<RepoItemType, String> {


    @Override
    public String convertToDatabaseColumn(RepoItemType repositoryTypeEnum) {
        return Arrays.stream(RepoItemType.values()).filter(a -> repositoryTypeEnum == a)
                .findFirst().map(Enum::name).orElse(StringUtils.EMPTY);
    }

    @Override
    public RepoItemType convertToEntityAttribute(String s) {
        return RepoItemType.valueOf(s);
    }
}
