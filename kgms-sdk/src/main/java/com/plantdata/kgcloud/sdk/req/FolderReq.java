package com.plantdata.kgcloud.sdk.req;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-07 10:10
 **/
@Data
public class FolderReq {
    @NotEmpty
    private String folderName;
}
