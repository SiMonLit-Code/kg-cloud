package com.plantdata.kgcloud.sdk.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-05 16:56
 **/
@Data
public class WordReq {
    private String name;

    private String syns;

    private String nature;

    /**
     * @description:
     * @author: Bovin
     * @create: 2019-11-05 11:38
     **/
    @ApiModel("模型调用")
    @Data
    public static class ModelCallReq {
        @ApiModelProperty(value = "模型名称", required = true)
        @NotNull
        @Size(min = 1)
        private List<String> input;
    }

    /**
     * @description:
     * @author: Bovin
     * @create: 2019-11-04 18:45
     **/
    @ApiModel("模型新增/修改")
    @Data
    public static class ModelReq {

        @ApiModelProperty(value = "模型名称", required = true)
        @NotNull
        private String modelName;

        @ApiModelProperty(value = "模型地址", required = true)
        @NotNull
        private String modelUrl;

        @ApiModelProperty(value = "模型类型", required = true)
        @NotNull
        private Integer modelType;

        @ApiModelProperty(value = "模型prf值")
        private String prf;

        @ApiModelProperty(value = "模型标签")
        private String labels;

        @ApiModelProperty(value = "模型描述")
        private String remark;
    }
}
