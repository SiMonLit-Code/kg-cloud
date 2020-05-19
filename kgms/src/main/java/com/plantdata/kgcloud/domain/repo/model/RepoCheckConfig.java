package com.plantdata.kgcloud.domain.repo.model;

import com.plantdata.kgcloud.domain.repo.enums.RepoCheckType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author cjw
 * @date 2020/5/18  16:20
 */
@Getter
@Setter
public class RepoCheckConfig {

    private RepoCheckType checkType;
    private int repositoryId;
    private String content;
}
