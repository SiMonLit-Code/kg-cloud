package com.plantdata.kgcloud.plantdata.req.rule;

import com.alibaba.fastjson.JSONObject;
import lombok.*;

import java.util.Map;
/**
 * @author Administrator
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class FormatParameter {
    private String kgName;
    @NonNull
    private Map<Integer, JSONObject> ruleSetting;
}
