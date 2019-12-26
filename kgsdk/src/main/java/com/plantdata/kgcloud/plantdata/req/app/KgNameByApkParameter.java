package com.plantdata.kgcloud.plantdata.req.app;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @author wuyue
 * @date 2019-06-18 18:41
 */
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class KgNameByApkParameter {

    @Min(1)
    Integer page = 1;
    @Min(1)
    @Max(50)
    Integer size = 10;

}
