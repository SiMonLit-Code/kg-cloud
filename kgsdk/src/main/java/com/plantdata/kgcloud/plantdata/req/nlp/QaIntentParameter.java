package com.plantdata.kgcloud.plantdata.req.nlp;

import lombok.*;


@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class QaIntentParameter {
   private String kgName;


    private String query;


    private  Integer size;

}
