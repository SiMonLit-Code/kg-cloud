package ai.plantdata.kgcloud.plantdata.req.explore.common;


import ai.plantdata.kgcloud.plantdata.bean.EntityLink;
import ai.plantdata.kgcloud.plantdata.req.common.DataLinks;
import ai.plantdata.kgcloud.plantdata.req.common.ExtraKVBean;
import ai.plantdata.kgcloud.plantdata.req.common.Tag;
import ai.plantdata.kgcloud.sdk.rsp.edit.DictRsp;
import ai.plantdata.kgcloud.sdk.rsp.edit.KnowledgeIndexRsp;
import ai.plantdata.kgcloud.sdk.rsp.edit.MultiModalRsp;
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
    private List<DictRsp> dictList;
    private List<KnowledgeIndexRsp> knowledgeIndexs;
    private List<MultiModalRsp> multiModals;
}
