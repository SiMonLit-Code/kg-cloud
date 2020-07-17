package ai.plantdata.kgcloud.plantdata.rsp.schema;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class TypeBean {
    private Long k;
    private String v;
    private Additional additionalInfo;
    private Long parentId;
    private String img;
}
