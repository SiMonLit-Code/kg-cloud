package com.plantdata.kgcloud.domain.app.dto;

import com.plantdata.kgcloud.domain.edit.util.VeDateUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/11 11:14
 */
@ToString
@Getter
@Setter
public class StatisticDateDTO extends StatisticDTO {
    private Date dateValue;

    public StatisticDateDTO(StatisticDTO resultsBean) {
        super(resultsBean);
        if (resultsBean.getValue() != null) {
            this.dateValue = VeDateUtils.strToDateLong(resultsBean.getValue());
        }
    }
}
