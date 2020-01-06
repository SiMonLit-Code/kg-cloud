package com.plantdata.kgcloud.domain.task.req;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-12 16:33
 **/
@Data
public class ImportTripleReq {

    private int type;
    private String kgName;
    private int mode;
    private Integer taskId;
    private List<String> dataIds;
}
