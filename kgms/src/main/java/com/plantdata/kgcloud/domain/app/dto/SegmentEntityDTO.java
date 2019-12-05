package com.plantdata.kgcloud.domain.app.dto;

import com.plantdata.kgcloud.sdk.rsp.app.explore.BasicEntityRsp;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/5 10:56
 */
@Getter
@Setter
@ToString
public class SegmentEntityDTO extends BasicEntityRsp {
    private String word;
    private Double score;
    private List<String> synonym;
}
