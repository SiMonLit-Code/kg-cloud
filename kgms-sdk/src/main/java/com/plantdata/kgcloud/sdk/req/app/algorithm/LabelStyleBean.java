package com.plantdata.kgcloud.sdk.req.app.algorithm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-05-26 17:39
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabelStyleBean {

    private String color;

    private Boolean inner;

    private Boolean visible;

}
