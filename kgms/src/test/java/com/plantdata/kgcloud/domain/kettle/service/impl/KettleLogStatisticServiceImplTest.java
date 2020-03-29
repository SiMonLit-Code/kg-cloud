package com.plantdata.kgcloud.domain.kettle.service.impl;

import com.plantdata.kgcloud.domain.app.util.JsonUtils;
import com.plantdata.kgcloud.domain.dw.rsp.GraphMapRsp;
import com.plantdata.kgcloud.domain.kettle.service.KettleLogStatisticService;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Administrator
 * @Description
 * @data 2020-03-29 16:36
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class KettleLogStatisticServiceImplTest {

    @Autowired
    private KettleLogStatisticService kettleLogStatisticService;

    @Test
    public void fillGraphMapRspCount() {
        GraphMapRsp graphMapRsp = new GraphMapRsp();
        graphMapRsp.setDataBaseId(83L);
        graphMapRsp.setTableName("paper");
        kettleLogStatisticService.fillGraphMapRspCount(Lists.newArrayList(graphMapRsp));
        System.out.println(JsonUtils.objToJson(graphMapRsp));

    }
}
