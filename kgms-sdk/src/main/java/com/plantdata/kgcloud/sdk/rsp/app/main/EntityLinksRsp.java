package com.plantdata.kgcloud.sdk.rsp.app.main;


import com.plantdata.kgcloud.sdk.rsp.app.explore.BasicEntityRsp;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author cjw 2019-11-01 15:11:39
 */
@Setter
@Getter
@ToString
public class EntityLinksRsp extends BasicEntityRsp {
    private List<ExtraRsp> extraList;
    @ApiModelProperty("关联的数据集")
    private List<DataLinkRsp> dataLinks;

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ExtraRsp {
        private Integer attrId;
        private String name;
        private Object value;
    }
}
