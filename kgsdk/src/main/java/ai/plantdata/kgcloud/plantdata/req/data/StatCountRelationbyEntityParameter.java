package ai.plantdata.kgcloud.plantdata.req.data;


import ai.plantdata.kgcloud.plantdata.bean.RelationbyFilterBean;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author Administrator
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class StatCountRelationbyEntityParameter {
    @NotBlank
    private String kgName;


    private Long entityId;

    private Boolean isDistinct = false;

    private List<RelationbyFilterBean<Integer>> allowAtts;

    private List<RelationbyFilterBean<Long>> allowTypes;


}
