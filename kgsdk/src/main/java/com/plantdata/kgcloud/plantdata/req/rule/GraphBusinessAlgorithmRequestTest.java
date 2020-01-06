package com.plantdata.kgcloud.plantdata.req.rule;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class GraphBusinessAlgorithmRequestTest {
    private String kgName;
    private String graphBean;
}
