package com.plantdata.kgcloud.sdk.rsp.app.main;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/21 15:02
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttributeExtraInfoItem {

    private Integer seqNo;
    private String name;
    private Integer dataType;
    private Integer type;
    private String dataUnit;
    private Integer indexed;
    private List<Long> objRange;
}
