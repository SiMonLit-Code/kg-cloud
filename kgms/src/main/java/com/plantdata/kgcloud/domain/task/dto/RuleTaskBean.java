package com.plantdata.kgcloud.domain.task.dto;

import com.plantdata.kgcloud.domain.task.dto.config.ConfigRuleBean;
import lombok.Data;

@Data
public class RuleTaskBean extends ConfigRuleBean {
    private Integer taskId;
    /**
     * 0 not started 1 ongoing 2 finished 3 failed
     */

    private Integer status;

}

