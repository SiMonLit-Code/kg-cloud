package com.plantdata.kgcloud.plantdata.req.semantic;


import com.plantdata.kgcloud.plantdata.req.common.PageModel;
import lombok.*;

/**
 * @author Administrator
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class GremlinParameter{

    String kgName;

    String gremlin;

}
