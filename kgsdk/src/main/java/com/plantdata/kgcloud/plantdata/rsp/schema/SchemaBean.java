package com.plantdata.kgcloud.plantdata.rsp.schema;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class SchemaBean {

    private List<TypeBean> types = new ArrayList<>();
    private List<AttBean> atts = new ArrayList<>();
    private List<AttrCategoryOutputBean> attGroup = new ArrayList<>();
    private String kgTitle;
}
