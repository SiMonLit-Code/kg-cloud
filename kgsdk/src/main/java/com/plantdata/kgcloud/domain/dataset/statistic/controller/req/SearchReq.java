package com.plantdata.kgcloud.domain.dataset.statistic.controller.req;

import lombok.Getter;
import lombok.Setter;

/**
 * @author cjw
 */
@Getter
@Setter
public class SearchReq extends SearchTableReq {
    private String dataName;

}
