package ai.plantdata.kgcloud.sdk.rsp.app.semantic;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ?
 */
@Getter
@Setter
public class TripleBean {
    private NodeBean start;
    private EdgeBean edge;
    private NodeBean end;

    private String reason;

}
