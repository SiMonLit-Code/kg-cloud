package ai.plantdata.kgcloud.domain.graph.config.entity;

import ai.plantdata.kgcloud.domain.common.converter.JsonNodeConverter;
import ai.plantdata.kgcloud.domain.graph.config.converter.ObjectConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Date;
import java.util.Map;

/**
 * @author Bovin
 * @description
 * @since 2020-08-12 16:51
 **/
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "graph_conf_snapshot")
@EntityListeners(AuditingEntityListener.class)
public class GraphConfSnapshot {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "kg_name")
    private String kgName;

    @Column(name = "spa")
    private String spa;

    @Basic
    @Column(name = "snapshot_config")
    @Convert(converter = ObjectConverter.class)
    private Object snapshotConfig;

    @Basic
    @Column(name = "create_at", updatable = false)
    @CreatedDate
    private Date createAt;

    @Basic
    @Column(name = "update_at")
    @LastModifiedDate
    private Date updateAt;

}
