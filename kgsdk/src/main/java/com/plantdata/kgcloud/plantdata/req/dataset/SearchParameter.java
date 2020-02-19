package com.plantdata.kgcloud.plantdata.req.dataset;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
@Data
public class SearchParameter {
    private String dataName;
    private String query;
    private List<String> fields;
    private String sort;
    @Min(1)
    private Integer pageNo = 1;
    @Max(30)
    @Min(1)
    private Integer pageSize = 10;
}
