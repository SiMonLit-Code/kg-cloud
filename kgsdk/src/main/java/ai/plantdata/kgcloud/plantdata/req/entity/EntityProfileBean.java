package ai.plantdata.kgcloud.plantdata.req.entity;


import ai.plantdata.kgcloud.plantdata.req.explore.common.EntityLinksBean;
import ai.plantdata.kgcloud.plantdata.req.common.KVBean;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class EntityProfileBean {

    private EntityLinksBean self;
    private List<EntityBean> pars;
    private List<EntityBean> sons;
    private Set<KVBean<String, List<EntityBean>>> atts;

    private Set<KVBean<String, List<EntityBean>>> reAtts;

    public void addAtts(String k, List<EntityBean> v) {

        if (this.atts == null) {
            this.atts = new HashSet<>();
        }

        this.atts.add(new KVBean<>(k, v));
    }

    public void addAtts(String k, List<EntityBean> v, Integer attrDefId) {

        if (this.atts == null) {
            this.atts = new HashSet<>();
        }

        this.atts.add(new KVBean<>(k, v, attrDefId));
    }

    public void addReAtts(String k, List<EntityBean> v) {

        if (this.reAtts == null) {
            this.reAtts = new HashSet<>();
        }

        this.reAtts.add(new KVBean<>(k, v));
    }

    public void addReAtts(String k, List<EntityBean> v, Integer attrDefId) {

        if (this.reAtts == null) {
            this.reAtts = new HashSet<>();
        }

        this.reAtts.add(new KVBean<>(k, v, attrDefId));
    }
}
