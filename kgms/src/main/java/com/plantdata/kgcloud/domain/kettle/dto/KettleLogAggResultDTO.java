package com.plantdata.kgcloud.domain.kettle.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Administrator
 * @Description
 * @data 2020-03-29 10:53
 **/
@Data

public class KettleLogAggResultDTO {

    private IdClass _id;
    private Long sum;


    @Data
    @AllArgsConstructor
    public static class IdClass {
        private String date;
        private String tbName;
        private Long dbId;
    }

    public IdClass get_id() {
        return _id;
    }

    public void set_id(IdClass _id) {
        this._id = _id;
    }
}
