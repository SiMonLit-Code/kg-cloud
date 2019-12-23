//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.plantdata.kgcloud.domain.wrap;

import com.hiekn.wrapper.bean.FieldConfigBean;
import com.hiekn.wrapper.bean.WrapperConfigBean;
import java.util.List;
import java.util.Map;

import com.hiekn.wrapper.service.StaticAct;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public interface StaticFetchKVAct extends StaticAct {
    Log LOGGER = LogFactory.getLog(StaticAct.class);

    Map<String, Object> execute(String var1, List<FieldConfigBean> var2);
}
