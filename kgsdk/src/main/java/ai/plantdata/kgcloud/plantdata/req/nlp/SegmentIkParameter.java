package ai.plantdata.kgcloud.plantdata.req.nlp;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class SegmentIkParameter {

    private String kw;


    private Integer mode;


    private Boolean abandonSingle;


    private  Boolean abandonRepeat;


}
