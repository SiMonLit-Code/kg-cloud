package ai.plantdata.kgcloud.plantdata.req.data;

import lombok.*;

/**
 * @author Administrator
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class MergeBean {
    private Long id;
    private Long toMergeId;
    private Double score;
    private Long classId;

}
