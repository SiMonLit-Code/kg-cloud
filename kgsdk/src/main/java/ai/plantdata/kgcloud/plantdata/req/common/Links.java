package ai.plantdata.kgcloud.plantdata.req.common;

/**
 * dataLinks
 * wuyue
 * 20190601
 */
public class Links {

    //数据ID
    private String dataId;

    //数据Title
    private String dataTitle;
    //权重
    private Double score;

    //来源 1.手工标引 2.自动标引
    private Integer source;

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getDataTitle() {
        return dataTitle;
    }

    public void setDataTitle(String dataTitle) {
        this.dataTitle = dataTitle;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }
}
