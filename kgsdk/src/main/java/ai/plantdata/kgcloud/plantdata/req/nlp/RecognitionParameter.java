package ai.plantdata.kgcloud.plantdata.req.nlp;

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
public class RecognitionParameter {

    private String kgName;
    private String kw;
    private Boolean useConcept;
    private Boolean useEntity;
    private Boolean useAttr;

}
