package com.plantdata.kgcloud.domain.repo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

/**
 * @author cjw
 * @date 2020/5/22  11:12
 */
@AllArgsConstructor
@Getter
public enum RepositoryLogEnum {

    MENU("menu"),
    REPOSITORY("repository");
    private String desc;

    public static Optional<RepositoryLogEnum> parseByDesc(String desc) {
        for (RepositoryLogEnum logEnum : RepositoryLogEnum.values()) {
            if (logEnum.desc.equals(desc)) {
                return Optional.of(logEnum);
            }
        }
        return Optional.empty();
    }
}
