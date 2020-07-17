package ai.plantdata.kgcloud.domain.graph.config.entity;

import ai.plantdata.kgcloud.domain.common.converter.LongListConverter;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/

@Data
@Entity
@Table(name = "graph_conf_qa")
@EntityListeners(AuditingEntityListener.class)
public class GraphConfQa {

    @Id
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "kg_name")
    private String kgName;

    @Basic
    @Column(name = "qa_type")
    private Integer type;

    @Basic
    @Column(name = "question")
    private String question;

    @Basic
    @Column(name = "count")
    private int count;

    @Basic
    @Column(name = "concept_ids")
    @Convert(converter = LongListConverter.class)
    private List<Long> conceptIds;

    @Basic
    @Column(name = "priority")
    private Integer priority;

    @Basic
    @Column(name = "create_at", updatable = false)
    @CreatedDate
    private Date createAt;

    @Basic
    @Column(name = "update_at")
    @LastModifiedDate
    private Date updateAt;

}
