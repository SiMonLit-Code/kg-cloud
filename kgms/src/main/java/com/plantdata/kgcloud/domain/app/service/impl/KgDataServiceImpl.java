package com.plantdata.kgcloud.domain.app.service.impl;

import com.google.common.collect.Lists;
import com.plantdata.kgcloud.domain.app.service.KgDataService;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/9 20:39
 */
public class KgDataServiceImpl implements KgDataService {
//
//    @Override
//    public void sparkSqlExport(String kgName) {
//
//        String exportName = "sparql_" + kgName + "_" + System.currentTimeMillis();
//
//        //查询搜索结果
//        QueryResultBean queryResultBean = sparqlQuery(SparqlQueryParameter.builder()
//                .kgName(kgName)
//                .query(sparkSqlExportParameter.getQuery())
//                .size(sparkSqlExportParameter.getSize())
//                .build()
//        );
//        List<List<SparqlNodeBean>> sparqlNodeBeans = queryResultBean.getResult();
//
//        if (sparkSqlExportParameter.getType() == 0) {
//            //导出txt
//            JsonUtils.exportJson(exportName, JsonUtils.toJson(sparqlNodeBeans));
//        } else if (sparkSqlExportParameter.getType() == 1 || sparkSqlExportParameter.getType() == 2) {
//            //创建标题
//            List<String> titleList = Lists.newArrayList();
//            if (sparqlNodeBeans != null && sparqlNodeBeans.size() > 0) {
//                List<SparqlNodeBean> sparqlNodeBeanList = sparqlNodeBeans.get(0);
//                if (sparqlNodeBeanList.size() > 0) {
//                    for (SparqlNodeBean sparqlNodeBean : sparqlNodeBeanList) {
//                        titleList.add(sparqlNodeBean.getKey());
//                    }
//                }
//            }
//
//            //导出excel
//            ExcelUtils.generateWorkbook(exportName, "xls", titleList, sparqlNodeBeans);
//        }
//    }

}
