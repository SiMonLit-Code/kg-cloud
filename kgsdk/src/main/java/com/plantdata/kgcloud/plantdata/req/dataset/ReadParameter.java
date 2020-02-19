package com.plantdata.kgcloud.plantdata.req.dataset;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class ReadParameter  {
    private String query;
    private String database;
    private String table;
    private List<String> fields;
    private String sort;
    private Integer pageNo = 1;
    @Max(30)
    private Integer pageSize = 10;
    @NotBlank
    private String dataName;

}
