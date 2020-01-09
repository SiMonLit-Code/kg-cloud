package com.plantdata.kgcloud.plantdata.req.common;

import com.plantdata.kgcloud.sdk.req.app.AttrSortReq;
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
public class AttrSortBean {
    private Integer attrId;
    private Integer seqNo;
    private Integer sort;

}
