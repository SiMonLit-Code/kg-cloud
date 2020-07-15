package ai.plantdata.kgcloud.sdk.rsp.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Collections;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/14 11:37
 */

@AllArgsConstructor
@Getter
@Setter
@ApiModel("分页结果")
public class PageRsp<T> {
    @ApiModelProperty("总数")
    private long total;
    @ApiModelProperty("数据")
    private List<T> data;

    public static <T> PageRsp<T> success(List<T> data, long total) {
        return new PageRsp<>(total, data);
    }

    public static <T> PageRsp<T> empty() {
        return new PageRsp<>(NumberUtils.LONG_ZERO, Collections.emptyList());
    }
}
