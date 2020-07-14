package ai.plantdata.kgcloud.plantdata.req.nlp;

import ai.plantdata.kgcloud.plantdata.rsp.MarkObject;
import lombok.Data;

import java.util.List;

/**
 * @author Administrator
 */
@Data
public class TagConfigSeq implements MarkObject {
    private List<TagConfig> tagConfigList;
}
