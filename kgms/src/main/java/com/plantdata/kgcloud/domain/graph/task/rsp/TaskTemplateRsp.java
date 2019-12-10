package com.plantdata.kgcloud.domain.graph.task.rsp;

import lombok.Data;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
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
