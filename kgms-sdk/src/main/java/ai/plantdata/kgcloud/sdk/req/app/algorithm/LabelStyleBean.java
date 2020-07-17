package ai.plantdata.kgcloud.sdk.req.app.algorithm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-05-26 17:39
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabelStyleBean {

    private String color;

    private Boolean inner;

    private Boolean visible;

    private String display;

    private Integer radius;

    private Double opacity;

    private String lineColor;

    private List<Integer> lineDash;

    private Integer lineWidth;




}
