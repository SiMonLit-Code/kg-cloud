package ai.plantdata.kgcloud.plantdata.rsp.app.statistic;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-06-07 15:16
 **/
@Getter
@Setter
@ApiModel("业务算法统计结构")
@NoArgsConstructor
@AllArgsConstructor
public class AlgorithmStatisticeBean {

    private List<Object> xAxis = new ArrayList<>();
    private List<AlgorithmStatisticeSeriesItemBean> series = new ArrayList<>();
}
