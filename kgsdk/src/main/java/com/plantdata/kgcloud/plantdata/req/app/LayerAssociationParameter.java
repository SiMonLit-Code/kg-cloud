package com.plantdata.kgcloud.plantdata.req.app;

import com.plantdata.kgcloud.plantdata.req.explore.common.AbstractGraphParameter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-06-13 15:53
 **/
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class LayerAssociationParameter {

    @NotBlank
    private String kgName;
    private Long entityId;
    private String kw;

    private Map<Integer, LayerKnowledgeParameter> layerFilter;

    @Min(0)
    private Integer pageSize = 10;

    @Min(1)
    private Integer pageNo = 1;
}
