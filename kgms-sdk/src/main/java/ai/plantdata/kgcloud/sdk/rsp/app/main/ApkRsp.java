package ai.plantdata.kgcloud.sdk.rsp.app.main;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/7 13:30
 */
@Getter
@Setter
@ToString
@ApiModel("apk和图谱信息")
@NoArgsConstructor
@AllArgsConstructor
public class ApkRsp {
    @ApiModelProperty("kgName")
    private String kgName;
    @ApiModelProperty("图谱中文名称")
    private String title;
    @ApiModelProperty("apk")
    private String apk;
}
