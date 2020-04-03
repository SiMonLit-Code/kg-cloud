package com.plantdata.kgcloud.domain.kettle.dto;

import com.plantdata.kgcloud.constant.KettleLogStatisticTypeEnum;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

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
        private Date date;
        private String tbName;
        private Long dbId;
    }

    private static final String MONTH_STR = "yyyy-MM";

    public IdClass get_id() {
        return _id;
    }

    public void set_id(IdClass _id) {
        this._id = _id;
    }

    public static String formatByStatisticType(Date date, KettleLogStatisticTypeEnum statisticType) {
        switch (statisticType) {
            case DAY:
                return DateUtils.formatDate(date, DateUtils.DATE_FORMAT);
            case HOUR:
                return DateUtils.formatDate(date, DateUtils.DATE_TIME_FORMAT);
            case MONTH:
                return DateUtils.formatDate(date, MONTH_STR);
            default:
                return null;
        }
    }
}
