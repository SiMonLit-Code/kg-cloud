package com.plantdata.kgcloud.sdk.req.app.algorithm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-05-26 17:40
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkStyleBean {

    private Boolean arrowVisible;

    private String color;

    private Integer length;

    private List<Integer> lineDash;

    private Integer radius;
}
