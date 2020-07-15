package ai.plantdata.kgcloud.sdk.rsp.app.semantic;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author ?
 */
@Getter
@Setter
public class GraphReasoningResultRsp {
    private int count;
    private List<TripleBean> tripleList;
}
