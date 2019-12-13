package com.plantdata.kgcloud.sdk.req.app.dataset;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 * @author cjw
 */
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class DataSetAddReq {
    @NotBlank
    private List<Map<String, Object>> dataList;
    @NotBlank
    private String dataName;

}
