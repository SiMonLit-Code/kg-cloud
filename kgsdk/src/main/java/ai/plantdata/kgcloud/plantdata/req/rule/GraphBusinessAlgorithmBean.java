package ai.plantdata.kgcloud.plantdata.req.rule;


import ai.plantdata.kgcloud.plantdata.rsp.MarkObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Administrator
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class GraphBusinessAlgorithmBean implements MarkObject {
    private Long id;
    private String kgName;
    private String name;
    private String abs;
    private String url;
    private Integer type;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT-5")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT-5")
    private Date updateTime;
}