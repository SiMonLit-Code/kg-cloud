package com.plantdata.kgcloud.domain.app.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/23 11:09
 */
@Getter
@Setter
@ToString
public class CoordinatesDTO {
    private Double x;
    private Double y;
    private Long cluster;
    private Long id;
    private Double distance;
}
