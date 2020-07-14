package ai.plantdata.kgcloud.domain.graph.attr.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Date;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "graph_attr_group_details")
@IdClass(GraphAttrGroupDetailsPk.class)
@EntityListeners(AuditingEntityListener.class)
public class GraphAttrGroupDetails {

    @Id
    @Column(name = "attr_id")
    private Integer attrId;

    @Id
    @Column(name = "group_id")
    private Long groupId;

    @Basic
    @Column(name = "create_at", updatable = false)
    @CreatedDate
    private Date createAt;

    @Basic
    @Column(name = "update_at")
    @LastModifiedDate
    private Date updateAt;

}
