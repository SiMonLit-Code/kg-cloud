package ai.plantdata.kgcloud.domain.graph.attr.entity;

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
public class GraphAttrGroupDetailsPk implements Serializable {
    @Column(name = "attr_id")
    @Id
    private Integer attrId;
    @Column(name = "group_id")
    @Id
    private Long groupId;


}
