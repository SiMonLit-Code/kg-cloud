package com.plantdata.kgcloud.domain.graph.manage.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GraphPk implements Serializable {
    @Column(name = "user_id")
    @Id
    private String userId;
    @Column(name = "kg_name")
    @Id
    private String kgName;

}
