package ai.plantdata.kgcloud.plantdata.rsp.schema;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Additional {
    private Boolean isOpenGis;
    private Map<String,Object> nodeStyle;
    private Map<String,Object> labelStyle;
    private Map<String,Object> linkStyle;

    private String color;
    private Map<String,Object> extra;
}
