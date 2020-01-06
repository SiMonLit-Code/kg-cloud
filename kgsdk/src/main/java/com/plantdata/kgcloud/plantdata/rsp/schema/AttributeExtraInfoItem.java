package com.plantdata.kgcloud.plantdata.rsp.schema;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Administrator
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class AttributeExtraInfoItem {
    private int seqNo;
    private String name;
    private int dataType;
    private int type;
    private String objRange;
    private String dataUnit;
    private int indexed;
}
