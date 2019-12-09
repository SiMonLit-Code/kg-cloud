package com.plantdata.kgcloud.sdk.rsp.edit;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Administrator
 */
@Getter
@Setter
@ApiModel
public class DeleteResult {
    private Long id;
    private String message;
}