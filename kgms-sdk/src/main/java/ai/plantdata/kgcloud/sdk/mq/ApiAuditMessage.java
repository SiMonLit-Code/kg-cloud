package ai.plantdata.kgcloud.sdk.mq;

import ai.plantdata.kgcloud.sdk.constant.ApiAuditStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApiAuditMessage implements Serializable {
    private String kgName;
    private String page;
    private String url;
    private ApiAuditStatusEnum status;
}
