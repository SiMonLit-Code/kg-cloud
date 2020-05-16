package com.plantdata.kgcloud.sdk.constant;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class PageableForFeign extends PageRequest {
    private static final long serialVersionUID = -4541509938956089562L;

    public PageableForFeign() {
        super(0,0);
    }
}
