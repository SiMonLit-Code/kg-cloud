package ai.plantdata.kgcloud.sdk.rsp.app;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Collections;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/12 15:09
 */
@Getter
@Setter
public class RestData<T> {

    private List<T> rsData;
    private Long rsCount;

    public RestData(List<T> rsData, Long rsCount) {
        this.rsData = rsData;
        this.rsCount = rsCount;
    }

    public static <T> RestData<T> empty() {
        return new RestData<>(Collections.emptyList(), NumberUtils.LONG_ZERO);
    }

}
