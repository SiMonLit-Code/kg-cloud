package ai.plantdata.kgcloud.sdk.rsp.app.nlp;

import lombok.Getter;
import lombok.Setter;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/4 17:36
 */
@Getter
@Setter
public class NamedEntityRsp {
    private String name;
    private String tag;
    private int pos;
}
