package com.plantdata.kgcloud.domain.scene.rsp;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NlpRsp {

    @ApiModelProperty(value = "模型id")
    private Long id;

    @ApiModelProperty(value = "请求地址")
    private String modelApi;

    @ApiModelProperty(value = "标签映射 key：模型识别的tag， value：映射图谱概念")
    private List<KVRsp> tags;

}
