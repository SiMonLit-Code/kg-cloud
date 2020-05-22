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

    MENU("menu", 1),
    REPOSITORY("repository", 2);
    private String desc;
    private int id;

    public static Optional<RepositoryLogEnum> parseByDesc(String desc) {
        for (RepositoryLogEnum logEnum : RepositoryLogEnum.values()) {
            if (logEnum.desc.equals(desc)) {
                return Optional.of(logEnum);
            }
        }
        return Optional.empty();
    }

    public static Optional<RepositoryLogEnum> parseById(int id) {
        for (RepositoryLogEnum logEnum : RepositoryLogEnum.values()) {
            if (logEnum.id==id) {
                return Optional.of(logEnum);
            }
        }
        return Optional.empty();
    }
}
