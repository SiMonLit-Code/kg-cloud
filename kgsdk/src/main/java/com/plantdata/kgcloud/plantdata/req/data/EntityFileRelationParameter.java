package com.plantdata.kgcloud.plantdata.req.data;

import com.plantdata.kgcloud.sdk.req.EntityFileRelationAddReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author lp
 * @date 2020/5/21 21:28
 */
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class EntityFileRelationParameter {
    @NotBlank
    private String kgName;
    @NotNull
    private Integer indexType;
    @NotEmpty
    private List<Long> entityIds;
    private String fileId;
    private String title;
    private String keyword;
    private String description;
    private String url;

}
