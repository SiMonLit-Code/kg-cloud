package ai.plantdata.kgcloud.plantdata.req.entity;

import ai.plantdata.kgcloud.plantdata.req.common.EntityMetaData;
import ai.plantdata.kgcloud.plantdata.req.common.KVBean;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class EntityBean extends EntityMetaData {


    private Long id;
    private String name;
    private Long classId;
    private List<Long> classIdList;
    private Long conceptId;
    private List<Long> conceptIdList;
    private String conceptName;
    private String className;
    private String meaningTag;
    // 0概念 1实例 默认实例
    private Integer type = 1;
    private String img;
    private Set<KVBean<String, Object>> extra;
    private boolean qa;


    public void addExtra(String k, String v) {
        if (this.extra == null) {
            this.extra = new HashSet<>();
        }
        KVBean<String, Object> eib = new KVBean<>(k, v);
        this.extra.add(eib);
    }

    public void addExtra(String k, Object v, Integer type) {
        if (this.extra == null) {
            this.extra = new HashSet<>();
        }
        KVBean<String, Object> eib = new KVBean<>(k, v);
        eib.setType(type);
        this.extra.add(eib);
    }

    public void addExtra(String k, Object v, Integer type, Integer attDefId) {
        if (this.extra == null) {
            this.extra = new HashSet<>();
        }
        KVBean<String, Object> eib = new KVBean<>(k, v, attDefId);
        eib.setType(type);
        this.extra.add(eib);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EntityBean that = (EntityBean) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getLowerName() {
        return StringUtils.isEmpty(this.name) ? this.name : this.name.toLowerCase();
    }

}
