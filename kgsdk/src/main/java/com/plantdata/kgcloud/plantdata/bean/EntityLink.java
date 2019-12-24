package com.plantdata.kgcloud.plantdata.bean;

import lombok.Data;

import java.util.Objects;

/**
 * @author Administrator
 */
@Data
public class EntityLink {
    private String kgTitle;
    private String kgName;
    private String entityName;
    private Long entityId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EntityLink that = (EntityLink) o;
        return kgName.equals(that.kgName) &&
                entityId.equals(that.entityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kgName, entityId);
    }
}
