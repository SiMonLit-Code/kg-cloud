package ai.plantdata.kgcloud.plantdata.rsp.schema;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class AttrCategoryOutputBean {
    private Long id;
    private String name;
    private List<Integer> attrDefIds;
}
