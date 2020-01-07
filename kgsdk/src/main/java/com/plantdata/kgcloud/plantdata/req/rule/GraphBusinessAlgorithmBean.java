package com.plantdata.kgcloud.plantdata.req.rule;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.plantdata.kgcloud.plantdata.rsp.MarkObject;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT-5")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT-5")
    private Date updateTime;
}