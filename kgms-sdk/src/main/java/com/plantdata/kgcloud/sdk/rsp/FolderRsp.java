package com.plantdata.kgcloud.sdk.rsp;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-07 10:12
 **/
@Data
public class FolderRsp {
    private Long id;

    private String userId;

    private String folderName;

    private Boolean defaulted;

    private Date createAt;

    private Date updateAt;
}
