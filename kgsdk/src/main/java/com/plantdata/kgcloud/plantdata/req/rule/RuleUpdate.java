package com.plantdata.kgcloud.plantdata.req.rule;

import com.plantdata.kgcloud.plantdata.bean.rule.RuleBean;
import lombok.*;

/**
 * @author Administrator
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class RuleUpdate {
    private String kgName;
    private RuleBean bean;
    private Integer id;
}
