package com.plantdata.kgcloud.domain.dw.entity;

import com.plantdata.kgcloud.domain.dw.converter.DataMapRspConverter;
import com.plantdata.kgcloud.domain.dw.converter.QuoteRspConverter;
import com.plantdata.kgcloud.sdk.req.DataMapReq;
import com.plantdata.kgcloud.sdk.req.SchemaQuoteReq;
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
    @Column(name = "map_config")
    @Convert(converter = QuoteRspConverter.class)
    private List<SchemaQuoteReq> mapConfig;

    @Basic
    @Column(name = "map_json")
    @Convert(converter = DataMapRspConverter.class)
    private List<DataMapReq> mapJson;

    @Basic
    @Column(name = "create_at")
    @CreatedDate
    private Date createAt;

    @Basic
    @Column(name = "update_at")
    @LastModifiedDate
    private Date updateAt;
}
