package com.plantdata.kgcloud.plantdata.req.data;

import com.plantdata.kgcloud.plantdata.req.common.PageModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-06-07 18:32
 **/
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class TraceabilityParameter extends PageModel {

    private String kgName;

    private String dataName;

    private String tableName;

    private String id;
}
