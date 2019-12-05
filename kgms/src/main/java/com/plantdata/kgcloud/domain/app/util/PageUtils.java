package com.plantdata.kgcloud.domain.app.util;

import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/29 16:40
 */
public class PageUtils {

    public static <T> List<T> subList(Integer pageNo, Integer pageSize, List<T> list) {
        int start;
        int length;
        if (pageSize <= 0) {
            pageSize = 10;
        }
        if (pageNo == 0) {
            pageNo = 1;
        }
        start = (pageNo - 1) * pageSize;
        length = pageSize;
        if (CollectionUtils.isEmpty(list) || start > list.size()) {
            return Collections.emptyList();
        }
        if (length > list.size()) {
            length = list.size();
        }
        return list.subList(start, length);
    }
}
