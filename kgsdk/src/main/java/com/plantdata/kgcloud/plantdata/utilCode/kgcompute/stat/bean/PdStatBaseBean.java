package com.plantdata.kgcloud.plantdata.utilCode.kgcompute.stat.bean;

public class PdStatBaseBean {


    public enum AggregatorEnum {

        SUM("SUM"),
        COUNT("COUNT"),
        MIN("MIN"),
        MAX("MAX"),
        AVG("AVG");

        private String value;

        AggregatorEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum OrderEnum {

        DESC("DESC"),
        ASC("ASC");

        private String value;

        OrderEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum FilterOperEnum {

        GT(">"),
        GTE(">="),
        EQ("="),
        LT("<"),
        LTE("<="),
        NE("<>"),
        IN("IN");

        private String value;

        FilterOperEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum FilterBoolEnum {

        AND("AND"),
        OR("OR");

        private String value;

        FilterBoolEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private String name;
    private AggregatorEnum aggregator;
    private String alias;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AggregatorEnum getAggregator() {
        return aggregator;
    }

    public void setAggregator(AggregatorEnum aggregator) {
        this.aggregator = aggregator;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
