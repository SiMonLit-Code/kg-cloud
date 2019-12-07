package com.plantdata.kgcloud.domain.document.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AttrReq {

    @ApiModelProperty(value = "属性的ID")
    private Integer id;

    @ApiModelProperty(value = "所属概念")
    private Integer conceptId;

    @ApiModelProperty(value = "所属文档")
    private Integer documentId;

    @ApiModelProperty(value = "文档中属性名称")
    private String name;

    @ApiModelProperty(value = "原名，对属性改名后会把原来名称存储在这个字段")
    private String oldName;

    @ApiModelProperty(value = "属性类别 0：数值属性 1：对象属性 -1：私有属性")
    private Integer type;

    @ApiModelProperty(value = "属性类型 1整数值 2浮点值 4日期时间 41日期 42时间 5字符串 6计算属性 8Map型 10文本型")
    private Integer dataType;

    @ApiModelProperty(value = "属性值域，当属性为对象属性时有值")
    private String rangeTo;

    //状态字段
    @ApiModelProperty(value = "是否忽略属性",name = "isIgnore")
    private Boolean isIgnore;

    @ApiModelProperty(value = "是否入图",name = "isImportGraph")
    private Boolean isImportGraph;


    //例式
    @ApiModelProperty(value = "出现的次数")
    private Integer count;

    @ApiModelProperty(value = "属性值示例")
    private String ps;


    //映射对象
    @ApiModelProperty(value = "映射匹配的属性名")
    private String attName;

    @ApiModelProperty(value = "映射匹配的属性id")
    private Integer attId;

    @ApiModelProperty(value = "映射的图谱属性类别 0：数值属性 1：对象属性")
    private Integer attType;

    @ApiModelProperty(value = "映射的图谱属性类型")
    private Integer attDataType;

    @ApiModelProperty(value = "映射的图谱对象属性值域")
    private String attRange;


    @ApiModelProperty(value = "属性状态 /**\n" +
            "        * 属性状态枚举\n" +
            "        * 新增：文档中的名称在原图谱中不存在\n" +
            "        * 已匹配： 文档中名称与原图谱名称对应且类型一致\n" +
            "        * 类型冲突：文档中名称与原图谱名臣个对应但类型不一致，包含数值类型定义不一致与数值、对象属性不一致\n" +
            "        * 已合并：文档书名名称手动改成原图谱中的某一个属性名称，相比已匹配状态，已合并多了一个改名\n" +
            "        * 已忽略：文档中的某个属性不设置入图\n" +
            "         */")
    private String attStatus;
}
