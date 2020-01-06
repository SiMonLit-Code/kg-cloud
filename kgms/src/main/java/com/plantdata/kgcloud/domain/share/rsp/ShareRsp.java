package com.plantdata.kgcloud.domain.share.rsp;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-09 19:47
 **/
@Data
public class ShareRsp {

    private Long id;

    private String userId;

    private String kgName;

    private String spaId;

    private String shareLink;

    private Boolean shared;

    private Long totalScan;

    private Date expireAt;

    private Date createAt;

    private Date updateAt;
}
