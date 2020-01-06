package com.plantdata.kgcloud.sdk.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Objects;

/**
 * @Author: LinHo
 * @Date: 2019/11/18 14:31
 * @Description:
 */
@Data
@ApiModel("实体链接查询结果")
public class EntityLinkVO {

    @ApiModelProperty(value = "标题")
    private String kgTitle;

    @ApiModelProperty(value = "图谱名称")
    private String kgName;

    @ApiModelProperty(value = "实体名称")
    private String entityName;

    @ApiModelProperty(value = "实体id")
    private Long entityId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EntityLinkVO vo = (EntityLinkVO) o;
        return kgName.equals(vo.kgName) &&
                entityId.equals(vo.entityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kgName, entityId);
    }
}
