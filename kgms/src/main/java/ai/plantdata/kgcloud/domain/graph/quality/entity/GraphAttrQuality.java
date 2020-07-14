package ai.plantdata.kgcloud.domain.graph.quality.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Author: LinHo
 * @Date: 2020/3/21 14:03
 * @Description:
 */
@Getter
@Setter
@Entity
@Table(name = "graph_attr_quality")
@EntityListeners(AuditingEntityListener.class)
public class GraphAttrQuality {
    @Id
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "self_id")
    private Long selfId;

    @Basic
    @Column(name = "kg_name")
    private String kgName;

    @Basic
    @Column(name = "attr_name")
    private String attrName;

    @Basic
    @Column(name = "attr_count")
    private Long attrCount;

    @Basic
    @Column(name = "attr_integrity")
    private Double attrIntegrity;

    @Basic
    @Column(name = "attr_reliability")
    private Double attrReliability;

    @Basic
    @Column(name = "create_at", updatable = false)
    @CreatedDate
    private Date createAt;

    @Basic
    @Column(name = "update_at")
    @LastModifiedDate
    private Date updateAt;

}
