package ai.plantdata.kgcloud.domain.edit.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lp
 * @date 2020/5/27 14:58
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityFileRelationScore {

    @ApiModelProperty("实体Id")
    private Long entityId;

    @ApiModelProperty("得分")
    private Double score;

    @ApiModelProperty("标引类别(0：手动标引，1：自动标引)")
    private Integer type;

}
