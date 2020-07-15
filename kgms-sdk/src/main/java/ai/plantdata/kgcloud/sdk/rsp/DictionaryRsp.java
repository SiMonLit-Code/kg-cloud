package ai.plantdata.kgcloud.sdk.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
@ApiModel("词典响应")
@Data
public class DictionaryRsp {

    @ApiModelProperty(value = "词典id")
    private Long id;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "词典名称")
    private String title;

    @ApiModelProperty(value = "词典描述")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private Date createAt;

    @ApiModelProperty(value = "修改时间")
    private Date updateAt;
}
