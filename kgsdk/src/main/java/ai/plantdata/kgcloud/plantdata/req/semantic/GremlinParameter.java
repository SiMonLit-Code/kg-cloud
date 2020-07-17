package ai.plantdata.kgcloud.plantdata.req.semantic;


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
