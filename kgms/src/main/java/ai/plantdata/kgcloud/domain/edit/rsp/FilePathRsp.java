package ai.plantdata.kgcloud.domain.edit.rsp;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: LinHo
 * @Date: 2019/11/29 16:30
 * @Description:
 */
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel("文件上传返回路径")
public class FilePathRsp {

    @ApiModelProperty(value = "路径")
    private String fullPath;

    @ApiModelProperty(value = "文件名称")
    private String fileName;
}
