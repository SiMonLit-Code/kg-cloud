package com.plantdata.kgcloud.plantdata.req.explore;


import com.plantdata.kgcloud.plantdata.req.common.DataLinks;
import com.plantdata.kgcloud.plantdata.req.common.EntityLink;
import com.plantdata.kgcloud.plantdata.req.common.ExtraKVBean;
import com.plantdata.kgcloud.plantdata.req.common.Tag;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Setter
@Getter
public class EntityLinksBean {
    private Long id;
    private String name;
    private Long classId;
    private String meaningTag;
    private Integer type = 1;
    private String img;
    private List<ExtraKVBean> extra;
    private List<DataLinks> dataLinks;
    private List<Tag> tags;
    private Set<EntityLink> entityLinks;
}
