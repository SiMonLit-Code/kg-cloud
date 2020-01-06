package com.plantdata.kgcloud.plantdata.req.config;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

/**
 * @author Administrator
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class InitStatisticalBean {
    private Long id;

    private String kgName;

    private String type;
    private Map<String, Object> rule;
    private Date createTime;
    private Date updateTime;
}