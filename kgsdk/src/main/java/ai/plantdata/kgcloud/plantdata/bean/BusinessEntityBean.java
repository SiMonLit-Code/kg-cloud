package ai.plantdata.kgcloud.plantdata.bean;

import ai.plantdata.kgcloud.plantdata.req.common.KVBean;
import ai.plantdata.kgcloud.plantdata.req.common.Tag;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @author Administrator
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class BusinessEntityBean {

    private Map<String, Object> style;
    private Long id;
    private String name;
    private Long classId;
    private Long conceptId;
    private String conceptName;
    private String className;
    private String meaningTag;
    /**
     * 0概念 1实例 默认实例
     */
    private Integer type = 1;
    private String img;
    private Double score = 0.0;
    private List<Tag> tags;
    private List<KVBean<String, Object>> extra;
    private String fromTime;
    private String toTime;
    private Set<EntityLink> entityLinks = new HashSet<>();


}
