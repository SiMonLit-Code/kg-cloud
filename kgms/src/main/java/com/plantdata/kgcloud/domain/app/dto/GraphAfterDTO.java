package com.plantdata.kgcloud.domain.app.dto;

import com.plantdata.kgcloud.domain.app.converter.BasicConverter;
import com.plantdata.kgcloud.sdk.req.app.function.GraphReqAfterInterface;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/19 9:53
 */
@Getter
@Setter
@ToString
public class GraphAfterDTO implements GraphReqAfterInterface {

    private List<Long> replaceClassIds;
    private Boolean relationMerge;

    public GraphAfterDTO(List<Long> replaceClassIds, boolean relationMerge) {
        BasicConverter.consumerIfNoNull(replaceClassIds, a -> this.replaceClassIds = replaceClassIds);
        this.relationMerge = relationMerge;
    }

    @Override
    public boolean isRelationMerge() {
        return relationMerge;
    }


}
