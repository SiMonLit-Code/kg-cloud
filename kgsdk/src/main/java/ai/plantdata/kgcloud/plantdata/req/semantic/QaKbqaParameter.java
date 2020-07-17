package ai.plantdata.kgcloud.plantdata.req.semantic;


import ai.plantdata.kgcloud.plantdata.req.common.PageModel;
import lombok.*;

/**
 * @author Administrator
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class QaKbqaParameter extends PageModel {

    String kgName;

    String query;



}
