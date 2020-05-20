package com.plantdata.kgcloud.domain.repo.model;

import com.plantdata.kgcloud.domain.repo.enums.RepoCheckType;
import com.plantdata.kgcloud.domain.repo.model.req.DealDTO;

import java.util.function.Function;

/**
 * @author cjw
 * @date 2020/5/15  12:02
 */
public interface RepositoryRoot {

    RepoCheckType checkType();

    DealDTO getBasicReq();

    Function<DealDTO,DealDTO> BasicRequest();
}
