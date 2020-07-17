package ai.plantdata.kgcloud.plantdata.req.nlp;

import lombok.*;

import javax.validation.constraints.NotBlank;
/**
 * @author Administrator
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class SegmentParametet {
    @NotBlank
    String kgName;
    @NotBlank
    String kw;

    Boolean useConcept = true;

    Boolean useEntity = true;

    Boolean useAttr = true;

}
