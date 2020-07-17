package ai.plantdata.kgcloud.sdk.req.app.algorithm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-05-26 17:36
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NodeStyleBean {

    private String color;

    private String imageMode;

    private Boolean imageVisible;

    private Integer radius;

    private String display;

    private String lineColor;

    private List<Integer> lineDash;

    private Integer lineWidth;
}
