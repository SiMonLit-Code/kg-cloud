package com.plantdata.kgcloud.domain.edit.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @Author: LinHo
 * @Date: 2019/12/6 10:23
 * @Description:
 */
@Data
@ApiModel("三元组带属性模型")
public class TripleRsp {

    @ApiModelProperty(value = "关系主体")
    private Subject subject;

    @ApiModelProperty(value = "关系标签")
    private Predicate predicate;

    @ApiModelProperty(value = "关系客体")
    private Target object;

    @Data
    public class Subject {

        @ApiModelProperty(value = "实体id")
        private Long id;

        @ApiModelProperty(value = "实体名称")
        private String name;
    }

    @Data
    public class Predicate {

        @ApiModelProperty(value = "属性id")
        private Integer id;

        @ApiModelProperty(value = "属性名称")
        private String name;

        @ApiModelProperty(value = "属性类型")
        private Integer type;
    }

    @Data
    public class Target {

        @ApiModelProperty(value = "客体实体id")
        private Long id;

        @ApiModelProperty(value = "客体值")
        private Object value;
    }
}
