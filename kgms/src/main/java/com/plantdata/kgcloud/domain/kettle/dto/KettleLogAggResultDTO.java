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
    private Long W;

    @Data
    @AllArgsConstructor
    public static class IdClass {
        private String date;
        private String resourceName;
    }

    public IdClass get_id() {
        return _id;
    }

    public void set_id(IdClass _id) {
        this._id = _id;
    }

    public Long getW() {
        return W;
    }

    public void setW(Long w) {
        W = w;
    }
}
