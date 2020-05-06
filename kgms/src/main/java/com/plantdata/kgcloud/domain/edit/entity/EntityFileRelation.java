package com.plantdata.kgcloud.domain.edit.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * @author lp
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityFileRelation {

    private String id;

    @ApiModelProperty("图谱名称")
    private String kgName;

    @ApiModelProperty("实体ID")
    private Long entityId;

    @ApiModelProperty("标引文件ID")
    private String dwFileId;

    @ApiModelProperty("标引类型(0：文件,1：文本,2：链接)")
    private Integer indexType;

    @ApiModelProperty("创建时间")
    private Date createTime;

}
