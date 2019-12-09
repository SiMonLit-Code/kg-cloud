package com.plantdata.kgcloud.domain.j2r.service;

import org.springframework.data.domain.Page;

/**
 * @author xiezhenxiang 2019/12/9
 */
public interface J2rService {

    Page<String> jsonStr(Integer dataSetId, Integer index);
}
