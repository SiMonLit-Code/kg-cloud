package ai.plantdata.kgcloud.sdk.req;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-09 22:07
 **/
@Data
public class AnnotationDataReq {

    @ApiParam(required = true, value = "数据集ID")
    @NotNull
    private Long id;

    @ApiParam(required = true, value = "数据Id")
    @NotNull
    private String objId;

    @ApiParam(required = true, value = "标引数据")
    private Map<String, Object> data;
}
