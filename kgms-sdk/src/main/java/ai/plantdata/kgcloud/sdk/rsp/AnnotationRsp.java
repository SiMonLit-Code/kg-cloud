package ai.plantdata.kgcloud.sdk.rsp;

import ai.plantdata.kgcloud.sdk.req.AnnotationConf;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-09 21:06
 **/
@Data
public class AnnotationRsp {

    private Long id;

    private Integer taskId;

    private String userId;

    private String kgName;

    private Long dataId;

    private String name;

    private List<AnnotationConf> config;

    private String description;

    private Date createAt;

    private Date updateAt;
}
