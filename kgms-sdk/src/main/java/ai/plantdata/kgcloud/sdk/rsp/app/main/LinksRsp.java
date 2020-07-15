package ai.plantdata.kgcloud.sdk.rsp.app.main;

import lombok.Getter;
import lombok.Setter;

/**
 * dataLinks
 *
 * @author wuyue
 * 2019-11-01 15:15:17
 */
@Getter
@Setter
public class LinksRsp {

    /**
     * 数据ID
     */
    private String dataId;
    /**
     * 数据Title
     */
    private String dataTitle;
    /**
     * 权重
     */
    private Double score;
    /**
     * 来源 1.手工标引 2.自动标引
     */
    private Integer source;
}
