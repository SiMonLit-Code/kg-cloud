package ai.plantdata.kgcloud.sdk.rsp.app.semantic;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author Administrator
 */
@Getter
@Setter
@ToString
public class ElementSeqBean {
    private List<ElementBean> sequence;
    private Double score;

}
