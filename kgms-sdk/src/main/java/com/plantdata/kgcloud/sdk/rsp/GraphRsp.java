package com.plantdata.kgcloud.sdk.rsp;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
@Data
public class GraphRsp {

    private String userId;
    private String kgName;
    private String title;
    private String icon;
    private Boolean privately;
    private Boolean editable;
    private Boolean deleted;
    private String remark;
    private Date createAt;
    private Date updateAt;
}
