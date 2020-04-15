package com.plantdata.kgcloud.domain.dw.rsp;

import io.micrometer.core.instrument.MultiGauge;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Row;

import java.util.List;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-03-25 17:34
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModelExcelRsp {

    private String name;

    private String meaningTag;

    private String parentName;

    private String parentMeaningTag;

    private List<Attr> attrs;

    private List<Relation> relations;

    private Row row;

    @Data
    public static class Attr{

        private String name;

        private String alias;

        private Integer dataType;

        private String unit;

    }

    @Data
    public static class Relation{

        private String domain;

        private String domainMeaningTag;

        private String name;

        private String alias;

        private String range;

        private String rangeMeaningTag;

        private List<Attr> relationAttrs;

    }
}


