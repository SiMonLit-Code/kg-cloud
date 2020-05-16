package com.plantdata.kgcloud.sdk.constant;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class PageForFeign<T> extends PageImpl<T> {
    private static final long serialVersionUID = 867755909294344406L;

    public PageForFeign(){
        super(new ArrayList<>());
    }
}
