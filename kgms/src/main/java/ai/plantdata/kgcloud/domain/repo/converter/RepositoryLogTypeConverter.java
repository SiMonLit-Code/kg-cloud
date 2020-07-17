package ai.plantdata.kgcloud.domain.repo.converter;

import ai.plantdata.kgcloud.domain.repo.enums.RepositoryLogEnum;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;

/**
 * @author cjw
 * @date 2020/5/22  13:02
 */
@Slf4j
public class RepositoryLogTypeConverter implements AttributeConverter<RepositoryLogEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(RepositoryLogEnum logEnum) {
        return logEnum.getId();
    }

    @Override
    public RepositoryLogEnum convertToEntityAttribute(Integer id) {
        return RepositoryLogEnum.parseById(id).orElse(null);
    }
}
