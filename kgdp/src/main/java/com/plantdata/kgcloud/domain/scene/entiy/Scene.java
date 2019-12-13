package com.plantdata.kgcloud.domain.scene.entiy;

import com.plantdata.kgcloud.domain.scene.rsp.ModelAnalysisRsp;
import com.plantdata.kgcloud.domain.scene.rsp.NlpRsp;
import com.plantdata.kgcloud.domain.scene.util.ListStringConvert;
import com.plantdata.kgcloud.domain.scene.util.ModelAnalysisConvert;
import com.plantdata.kgcloud.domain.scene.util.NlpConvert;
import com.plantdata.kgcloud.domain.scene.util.SchemaConvert;
import com.plantdata.kgcloud.sdk.rsp.app.main.SchemaRsp;
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
@Table(name = "scene")
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class Scene {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "user_id")
    private String userId;

    @Basic
    @Column(name = "abs")
    private String abs;

    @Basic
    @Column(name = "is_structure_dismantling")
    private Boolean structureDismantling;

    @Basic
    @Column(name = "dismantling_dimensionality")
    private Integer dismantlingDimensionality;

    @Basic
    @Column(name = "label_model")
    @Convert(converter = SchemaConvert.class)
    private SchemaRsp labelModel;

    @Basic
    @Column(name = "label_model_type")
    private Integer labelModelType;

    @Basic
    @Column(name = "knowledge_index")
    private Integer knowledgeIndex;

    @Basic
    @Column(name = "model_analysis")
    @Convert(converter = ModelAnalysisConvert.class)
    private List<ModelAnalysisRsp> modelAnalysis;

    @Basic
    @Column(name = "nlp")
    @Convert(converter = NlpConvert.class)
    private List<NlpRsp> nlp;

    @Basic
    @Column(name = "doc_format")
    private Integer docFormat;

    @Basic
    @Column(name = "doc_type")
    @Convert(converter = ListStringConvert.class)
    private List<String> docType;


    @Basic
    @Column(name = "create_at", updatable = false)
    @CreatedDate
    private Date createTime;

    @Basic
    @Column(name = "update_at")
    @LastModifiedDate
    private Date updateTime;
}
