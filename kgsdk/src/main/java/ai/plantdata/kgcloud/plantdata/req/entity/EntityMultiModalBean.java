package ai.plantdata.kgcloud.plantdata.req.entity;

import ai.plantdata.kgcloud.sdk.rsp.edit.MultiModalRsp;
import lombok.*;

import java.util.List;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-06-07 14:18
 **/
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class EntityMultiModalBean {

    private Long id;
    private String name;
    private Long classId;
    private String meaningTag;
    private Integer type = 1;
    private String img;
    private List<MultiModalRsp> multiModals;
}
