package ai.plantdata.kgcloud.plantdata.req.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Administrator
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class GisBean {
    private Boolean isOpenGis = true;
    private Double lng;
    private Double lat;
    private String address;

}
