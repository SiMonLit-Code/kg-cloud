package com.plantdata.kgcloud.domain.j2r.service.impl;

import com.plantdata.kgcloud.domain.j2r.service.J2rService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiezhenxiang 2019/12/9
 */
@Service
public class J2rServiceImpl implements J2rService {

    @Override
    public Page<String> jsonStr(Integer dataSetId, Integer index) {

        List<String> ls = new ArrayList<>();
        ls.add("test");
        ls.add("test2");

        PageImpl<String> page = new PageImpl<>(ls, PageRequest.of(index, 1), 10);
        return page;
    }
}
