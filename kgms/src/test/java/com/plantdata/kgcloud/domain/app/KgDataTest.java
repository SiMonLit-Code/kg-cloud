package com.plantdata.kgcloud.domain.app;

import ai.plantdata.kg.api.ql.SparqlApi;
import ai.plantdata.kg.api.ql.resp.QueryResultVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.plantdata.kgcloud.domain.app.service.KgDataService;
import com.plantdata.kgcloud.domain.app.util.JsonUtils;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import com.plantdata.kgcloud.sdk.req.app.dataset.NameReadReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EdgeAttrStatisticByAttrValueReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EdgeStatisticByConceptIdReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EdgeStatisticByEntityIdReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EntityStatisticGroupByAttrIdReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EntityStatisticGroupByConceptReq;
import com.plantdata.kgcloud.sdk.rsp.app.RestData;
import com.plantdata.kgcloud.sdk.rsp.app.statistic.EdgeStatisticByEntityIdRsp;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/18 14:22
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class KgDataTest {
    private static final String KG_NAME = "dh3773_9r96hk5ii5cfkk11";
    @Autowired
    private KgDataService kgDataService;
    @Autowired
    private SparqlApi sparqlApi;

    /**
     * sparQl
     */
    @Test
    public void sparQlTest() {
        String sparQl = "PREFIXrdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>PREFIXowl:<http://www.w3.org/2002/07/owl#>PREFIXrdfs:<http://www.w3.org/2000/01/rdf-schema#>PREFIXxsd:<http://www.w3.org/TR/2003/PR-owl-guide-20031209/wine#>SELECT?s?p?oWHERE{?s?p?o}";
        Optional<QueryResultVO> convert = RestRespConverter.convert(sparqlApi.query(KG_NAME, sparQl, 10));
        System.out.println(JacksonUtils.writeValueAsString(convert.get()));
    }

    @Test
    public void statisticCountEdgeByEntityTest() {
        EdgeStatisticByEntityIdReq req = new EdgeStatisticByEntityIdReq();
        req.setEntityId(3L);
        req.setIsDistinct(true);
        List<EdgeStatisticByEntityIdRsp> maps = kgDataService.statisticCountEdgeByEntity(KG_NAME, req);
        System.out.println(JacksonUtils.writeValueAsString(maps));
    }

    @Test
    public void statEntityGroupByConceptTest() {
        EntityStatisticGroupByConceptReq conceptReq = new EntityStatisticGroupByConceptReq();
        conceptReq.setEntityIds(Lists.newArrayList(3L,4L,13L,5L));
        conceptReq.setAllowTypes(Lists.newArrayList(1L, 2L));
        //conceptReq.setReturnType(1);
        Object obj = kgDataService.statEntityGroupByConcept(KG_NAME, conceptReq);
        System.out.println(JacksonUtils.writeValueAsString(obj));
    }

    /**
     * 统计属性根据概念分组
     */
    @Test
    public void statisticAttrGroupByConceptTest() {
        EntityStatisticGroupByAttrIdReq attrIdReq = new EntityStatisticGroupByAttrIdReq();
        attrIdReq.setEntityIds(Lists.newArrayList(1L, 2L, 4L, 3L));
        Object obj = kgDataService.statisticAttrGroupByConcept(KG_NAME, attrIdReq);
        System.out.println(JacksonUtils.writeValueAsString(obj));
    }

    @Test
    public void statisticRelationTest() {
        EdgeStatisticByConceptIdReq conceptIdReq = new EdgeStatisticByConceptIdReq();
        conceptIdReq.setConceptId(1L);
        Object obj = kgDataService.statisticRelation(KG_NAME, conceptIdReq);
        System.out.println(JacksonUtils.writeValueAsString(obj));
    }

    @Test
    public void statEdgeGroupByEdgeValueTest() {
        EdgeAttrStatisticByAttrValueReq attrValueReq = new EdgeAttrStatisticByAttrValueReq();
        attrValueReq.setAttrId(2);
        attrValueReq.setSeqNo(1);
        Object obj = kgDataService.statEdgeGroupByEdgeValue(KG_NAME, attrValueReq);
        System.out.println(JacksonUtils.writeValueAsString(obj));
    }

    @Test
    public void searchMongoDataSetTest() {
        NameReadReq nameReadReq = new NameReadReq();
        nameReadReq.setDataName("bj73pb33_dataset_aaabbb");
        RestData<Map<String, Object>> dataSet = kgDataService.searchDataSet("bj73pb33", nameReadReq);
        System.out.println(JacksonUtils.writeValueAsString(dataSet));
    }

    @Test
    public void searchEsDataSetTest() {
        NameReadReq nameReadReq = new NameReadReq();
        nameReadReq.setDataName("bj73pb33_dataset_shangchuande");

        nameReadReq.setSort("{\"_oprTime\":{\"order\":\"desc\"}}");
        RestData<Map<String, Object>> dataSet = kgDataService.searchDataSet("bj73pb33", nameReadReq);
        System.out.println(JacksonUtils.writeValueAsString(dataSet));
    }
}
