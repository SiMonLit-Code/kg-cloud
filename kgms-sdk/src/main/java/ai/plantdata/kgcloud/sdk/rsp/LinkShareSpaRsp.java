package ai.plantdata.kgcloud.sdk.rsp;

import lombok.Data;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-25 11:50
 **/
@Data
public class LinkShareSpaRsp {

    private String kgName;

    private String spaId;

    private Boolean shareable;

}
