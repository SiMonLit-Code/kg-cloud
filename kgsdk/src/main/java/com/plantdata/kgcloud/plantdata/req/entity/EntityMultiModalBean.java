package com.plantdata.kgcloud.plantdata.req.entity;

import com.plantdata.kgcloud.plantdata.bean.EntityLink;
import com.plantdata.kgcloud.plantdata.req.common.DataLinks;
import com.plantdata.kgcloud.plantdata.req.common.ExtraKVBean;
import com.plantdata.kgcloud.plantdata.req.common.Tag;
import com.plantdata.kgcloud.sdk.rsp.edit.DictRsp;
import com.plantdata.kgcloud.sdk.rsp.edit.KnowledgeIndexRsp;
import com.plantdata.kgcloud.sdk.rsp.edit.MultiModalRsp;
import lombok.*;

import java.util.List;
import java.util.Set;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-06-07 14:18
 **/
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class EntityMultiModalBean {

    private Long id;
    private String name;
    private Long classId;
    private String meaningTag;
    private Integer type = 1;
    private String img;
    private List<MultiModalRsp> multiModals;
}
