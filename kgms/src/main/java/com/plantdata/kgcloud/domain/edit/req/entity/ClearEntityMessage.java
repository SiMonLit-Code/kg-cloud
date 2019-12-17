package com.plantdata.kgcloud.domain.edit.req.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: LinHo
 * @Date: 2019/12/16 20:01
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClearEntityMessage {

    private Long id;

    private String kgName;

    private String taskType;

    private String params;

    private boolean finish;

}
