package com.plantdata.kgcloud.plantdata.req.nlp;

import lombok.*;

/**
 * @author Administrator
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class NerParameter {

    private String input;


    private TagConfigSeq config;

}
