package com.plantdata.kgcloud.plantdata.rsp.app;

import lombok.*;

/**
 * @author wuyue
 * @date 2019-06-18 18:35
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
public class ApkBean {
    private String kgName;
    private String title;
    private String apk;

}
