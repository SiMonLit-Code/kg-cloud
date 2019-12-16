package com.plantdata.kgcloud.plantdata.req.common;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class OriginBean {
    private String name;
    private String reason;
}
