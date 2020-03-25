package com.plantdata.kgcloud.domain.dw.entity;

import com.plantdata.kgcloud.domain.common.converter.IntegerListConverter;
import com.plantdata.kgcloud.domain.common.converter.LongListConverter;
import com.plantdata.kgcloud.domain.common.converter.StringListConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Builder
@Table(name = "dw_graph_map")
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class DWGraphMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "kg_name")
    private String kgName;

    @Basic
    @Column(name = "create_at")
    @CreatedDate
    private Date createAt;

    @Basic
    @Column(name = "update_at")
    @LastModifiedDate
    private Date updateAt;

    @Basic
    @Column(name = "model_id")
    private Integer modelId;

    @Basic
    @Column(name = "model_concept_id")
    private Integer modelConceptId;

    @Basic
    @Column(name = "entity_name")
    private String entityName;

    @Basic
    @Column(name = "concept_name")
    private String conceptName;

    @Basic
    @Column(name = "p_concept_name")
    private String pConceptName;

    @Basic
    @Column(name = "concept_id")
    private Long conceptId;

    @Basic
    @Column(name = "p_concept_id")
    private Long pConceptId;

    @Basic
    @Column(name = "table_name")
    private String tableName;

    @Basic
    @Column(name = "data_base_id")
    private Long dataBaseId;

    @Basic
    @Column(name = "model_attr_id")
    private Integer modelAttrId;

    @Basic
    @Column(name = "attr_id")
    private Integer attrId;

    @Basic
    @Column(name = "attr_name")
    private String attrName;

    @Basic
    @Column(name = "attr_key")
    private String attrKey;

    @Basic
    @Column(name = "attr_type")
    private Integer attrType;

    @Basic
    @Column(name = "data_type")
    private Integer dataType;

    @Basic
    @Column(name = "model_range")
    @Convert(converter = IntegerListConverter.class)
    private List<Integer> modelRange;

    @Basic
    @Column(name = "range")
    @Convert(converter = LongListConverter.class)
    private List<Long> range;

    @Basic
    @Column(name = "rangeName")
    @Convert(converter = StringListConverter.class)
    private List<String> rangeName;

    @Basic
    @Column(name = "alias")
    private String alias;

    @Basic
    @Column(name = "unit")
    private String unit;

    @Basic
    @Column(name = "scheduling_switch")
    private Integer schedulingSwitch;
}
