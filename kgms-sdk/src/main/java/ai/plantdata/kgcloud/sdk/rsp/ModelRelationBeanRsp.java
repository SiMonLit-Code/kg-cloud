package ai.plantdata.kgcloud.sdk.rsp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-04-17 17:48
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModelRelationBeanRsp {

    private String name;

    private String domain;

    private Set<String> range;

    private Set<ModelRelationAttrBeanRsp> attrs;

}
