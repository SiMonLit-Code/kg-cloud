package ai.plantdata.kgcloud.sdk.rsp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/9 11:26
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OpenBatchResult<T> {

    private List<T> success;
    private List<T> error;

    public static <T> OpenBatchResult<T> empty() {
        OpenBatchResult<T> openBatchResult = new OpenBatchResult<T>();
        openBatchResult.setError(Collections.emptyList());
        openBatchResult.setSuccess(Collections.emptyList());
        return openBatchResult;
    }
}
