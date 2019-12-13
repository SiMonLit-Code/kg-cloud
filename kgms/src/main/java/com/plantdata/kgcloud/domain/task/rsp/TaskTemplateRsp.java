package com.plantdata.kgcloud.domain.task.rsp;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
@Data
public class TaskTemplateRsp {


    private String title;

    private List<String> template;

}
