package com.plantdata.kgcloud.sdk.rsp.app.nlp;

import com.plantdata.kgcloud.sdk.rsp.app.explore.BasicEntityRsp;
import lombok.Getter;
import lombok.Setter;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/11 15:59
 */
@Getter
@Setter
public class DistanceEntityRsp extends BasicEntityRsp {

    private Double score;
}
