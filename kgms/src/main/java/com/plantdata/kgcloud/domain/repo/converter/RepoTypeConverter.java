package com.plantdata.kgcloud.domain.repo.converter;

import com.plantdata.kgcloud.domain.repo.enums.RepositoryTypeEnum;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.AttributeConverter;
import java.util.Arrays;

/**
 * @author cjw
 * @date 2020/5/19  15:21
 */
public class RepoTypeConverter implements AttributeConverter<RepositoryTypeEnum, String> {


    @Override
    public String convertToDatabaseColumn(RepositoryTypeEnum repositoryTypeEnum) {
        return Arrays.stream(RepositoryTypeEnum.values()).filter(a -> repositoryTypeEnum == a)
                .findFirst().map(Enum::name).orElse(StringUtils.EMPTY);
    }

    @Override
    public RepositoryTypeEnum convertToEntityAttribute(String s) {
        return RepositoryTypeEnum.valueOf(s);
    }
}
