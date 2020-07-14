package ai.plantdata.kgcloud.domain.graph.config.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/

@Data
public class GraphConfFocusPk implements Serializable {
    @Column(name = "kg_name")
    @Id
    private String kgName;

    @Column(name = "focus_type")
    @Id
    private String type;

}
