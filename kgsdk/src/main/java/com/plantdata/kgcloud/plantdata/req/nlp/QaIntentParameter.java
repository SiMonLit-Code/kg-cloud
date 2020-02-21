package com.plantdata.kgcloud.plantdata.req.nlp;

import lombok.*;

import javax.validation.constraints.NotNull;


@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class QaIntentParameter {
    private String kgName;


    private String query;

    @NotNull
    private Integer size = 5;

}
