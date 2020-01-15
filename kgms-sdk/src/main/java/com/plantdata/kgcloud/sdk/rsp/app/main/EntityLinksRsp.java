package com.plantdata.kgcloud.sdk.rsp.app.main;


import com.plantdata.kgcloud.sdk.rsp.EntityLinkVO;
import com.plantdata.kgcloud.sdk.rsp.app.explore.BasicEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.TagRsp;
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
    @ApiModelProperty("标签信息")
    private List<TagRsp> tags;
    @ApiModelProperty("实体关联")
    private List<EntityLinkVO> entityLinks;

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ExtraRsp {
        private Integer attrId;
        private String name;
        @ApiModelProperty("属性所属概念")
        private Long domainValue;
        private Object value;
        private Integer dataType;

        public ExtraRsp(Integer attrId, String name, Long domainValue, Integer dataType) {
            this.attrId = attrId;
            this.name = name;
            this.domainValue=domainValue;
            this.dataType = dataType;
        }

        public static ExtraRsp buildDefault(Integer attrId, String name, Object value) {
            ExtraRsp extraRsp = new ExtraRsp();
            extraRsp.attrId = attrId;
            extraRsp.name = name;
            extraRsp.value = value;
            return extraRsp;
        }

    }
}
