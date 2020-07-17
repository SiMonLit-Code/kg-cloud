package ai.plantdata.kgcloud.plantdata.req.data;

import ai.plantdata.kgcloud.plantdata.req.common.PageModel;
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
