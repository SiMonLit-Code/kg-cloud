package com.plantdata.kgcloud.domain.app.util;

import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/29 16:40
 */
public class PageUtils {

    public static final int DEFAULT_SIZE = 10;
    @Getter
    private Integer offset;
    @Getter
    private Integer limit;

    public PageUtils(Integer pageNo, Integer pageSize) {
        init(pageNo, pageSize);
    }

    public static <T> List<T> subList(Integer pageNo, Integer pageSize, List<T> list) {
        PageUtils pageUtils = new PageUtils(pageNo, pageSize);
        if (CollectionUtils.isEmpty(list) || pageUtils.offset > list.size()) {
            return Collections.emptyList();
        }
        if (pageUtils.limit > list.size()) {
            pageUtils.limit = list.size();
        }
        return list.subList(pageUtils.limit, pageUtils.offset);
    }

    private PageUtils init(Integer pageNo, Integer pageSize) {
        if (pageSize <= 0) {
            pageSize = DEFAULT_SIZE;
        }
        if (pageNo == 0) {
            pageNo = 1;
        }
        this.offset = (pageNo - 1) * pageSize;
        this.limit = pageSize;
        return this;
    }
}
