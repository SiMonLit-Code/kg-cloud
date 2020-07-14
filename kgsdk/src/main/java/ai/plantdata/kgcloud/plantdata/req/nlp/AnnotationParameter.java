package ai.plantdata.kgcloud.plantdata.req.nlp;

import lombok.*;

import java.util.List;

/**
 * @author Administrator
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class AnnotationParameter {

    private String kgName;
    private String text;
    private List<Long> conceptIds;
    private List<Long> conceptKeys;
}
