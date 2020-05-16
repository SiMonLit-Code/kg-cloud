package com.plantdata.kgcloud.sdk.constant;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;

import java.util.*;

public class SortForFeign extends Sort {
    private static final long serialVersionUID = 5737186511678863905L;

    public SortForFeign() {
        super(new ArrayList<>());
    }
}
