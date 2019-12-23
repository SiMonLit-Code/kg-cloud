package com.plantdata.kgcloud.sdk.req.app.sematic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @ProjectName: kg-services
 * @Package: ai.plantdata.kg.semantic.domain.qa.bean.req
 * @ClassName: NerSearchReq
 * @Author: zhangxc
 * @Description:
 * @Date: 2019/11/20 16:04
 * @Version: 1.0
 */
@Getter
@Setter
public class NerSearchReq {
    /**
     * 图谱名称
     */
    @NotBlank
    private String kgName;
    @ApiModelProperty("自然语言输入")
    private String query;
    @ApiModelProperty("切词模式，1 最多词，2 前向最大匹配，3 后向最大匹配，数字组合表示使用多种模式，例如13表示使用最多词和后向最大匹配")
    private int mode;
    /**
     * 识别的概念范围
     */
    private List<Long> concepts;
}
