package com.plantdata.kgcloud.service.impl;

import com.plantdata.kgcloud.service.MyDataUploadService;
import org.springframework.stereotype.Service;

@Service
public class MyDataUploadServiceImpl implements MyDataUploadService {
//
//    @Autowired
//    private MyDataMapper myDataMapper;
//
//    @Autowired
//    private MyDataMongoDriver myDataMongoDriver;
//
//    @Autowired
//    private UserApkMapper userApkBeanMapper;
//
//    @Value("${file_dir_path}")
//    private String file_dir_path;
//
//    @Autowired
//    private ConfigManage configManage;
//
//    @Override
//    public Map<String, Object> uploadData(Integer id, FormDataContentDisposition fileInfo, InputStream fileIn, FormDataBodyPart formDataBodyPart) {
//        MyDataBean myDataBean = myDataMapper.selectById(id);
//        if (myDataBean == null || (myDataBean.getDataType() != 2 && myDataBean.getDataType() != 1)) {
//            throw ServiceException.newInstance(ErrorCodes.MYDATA_NOTEXIST_ERROR);
//        }
//
//        String name = fileInfo.getFileName();
//        List<Map<String, Object>> dataList = null;
//        List<Map<String, Object>> errorList = new ArrayList<>();
//        if (name == null) {
//            throw ServiceException.newInstance(ErrorCodes.UPLAD_FILE_ERROR);
//        }
//        if (name.endsWith(".csv")) {
////            dataList = CSVUtils.importCsv(fileIn);
//        } else if (name.endsWith(".xls")||name.endsWith(".xlsx")) {
//            dataList = PoiUtils.importXls(fileIn, name);
//        } else if (name.endsWith(".json")) {
//            dataList = CommonUtils.importJson(formDataBodyPart);
//        } else {
//            throw ServiceException.newInstance(ErrorCodes.UPLAD_FILE_ERROR);
//        }
//
//        int successNum = 0;
//        int failureNum = 0;
//        if (!CollectionUtils.isNotEmpty(dataList)) {
//            throw RestException.newInstance(ErrorCodes.EXCEL_CONTENT_NULL_ERROR);
//        }
//
//        StringBuffer query = new StringBuffer();
//        RestClient restClient = null;
//        NStringEntity entity = null;
//        String table = "{ \"index\" : { \"_index\" : \"" + myDataBean.getDbName() + "\", \"_type\" : \"" + myDataBean.getTbName() + "\" } }";
//        int i = 0;
//
//        if (myDataBean.getDataType() == 2) {
//            for (Map<String, Object> one : dataList) {
//                i++;
//                JSONObject saveData = setDateWithSchema(JSONArray.parseArray(myDataBean.getSchemaConfig()), one);
//                if (saveData.containsKey("_error_code")) {
//                    one.put("error", saveData.getString("_error_code"));
//                    errorList.add(one);
//                    continue;
//                }
//
//                //添加检测字段
//                setTimeField(saveData);
//
//                query.append(table + "\n");
//                query.append(saveData.toString() + "\n");
//
//                if (i % 1000 == 0) {
//                    try {
//                        restClient = EsRestClient.getClient(myDataBean.getIp(), configManage.getCloudEsRestPort());
//                        String endpoint = "/_bulk?refresh";
//                        entity = new NStringEntity(query.toString(), ContentType.APPLICATION_JSON);
//                        Response response = restClient.performRequest("POST", endpoint, Collections.singletonMap("pretty", "true"), entity);
//                        String rs = EntityUtils.toString(response.getEntity());
//                        successNum++;
//
//                    } catch (Exception e) {
//                        failureNum++;
//                        throw RestException.newInstance(ErrorCodes.DATA_FORMAT_ERROR);
//                    } finally {
//                        if (entity != null) {
//                            entity.close();
//                        }
//                        if (Objects.nonNull(restClient)) {
//                            try {
//                                restClient.close();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                    query = new StringBuffer();
//                }
//
//            }
//            try {
//                restClient = EsRestClient.getClient(myDataBean.getIp(), configManage.getCloudEsRestPort());
//                String endpoint = "/_bulk?refresh";
//                entity = new NStringEntity(query.toString(), ContentType.APPLICATION_JSON);
//                Response response = restClient.performRequest("POST", endpoint, Collections.singletonMap("pretty", "true"), entity);
//                String rs = EntityUtils.toString(response.getEntity());
//                successNum++;
//            } catch (Exception e) {
//                failureNum++;
////                throw RestException.newInstance(ErrorCodes.DATA_FORMAT_ERROR);
//            } finally {
//                if (entity != null) {
//                    entity.close();
//                }
//                if (Objects.nonNull(restClient)) {
//                    try {
//                        restClient.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//        } else if (myDataBean.getDataType() == 1) {
//            List<JSONObject> jsonList = Lists.newArrayList();
//            for (Map<String, Object> one : dataList) {
//                JSONObject saveData = setDateWithSchema(JSONArray.parseArray(myDataBean.getSchemaConfig()), one);
//
////                saveData.put("oprTime", DateUtil.getMongoDate(new DATE()));
////                saveData.put("_oprTime", DateUtil.getMongoDate(new DATE()));
////                saveData.put("_persistTime",DateUtil.getMongoDate(new DATE()));
//
//                saveData.put("oprTime", new Date());
//                saveData.put("_oprTime", new Date());
//                saveData.put("_persistTime", new Date());
//                if (saveData.containsKey("_error_code")) {
//                    one.put("error", saveData.getString("_error_code"));
//                    errorList.add(one);
//                    continue;
//                }
//
//                jsonList.add(saveData);
//
//            }
//            try {
//
//                int count = 0;
//                List<List<JSONObject>> result = Lists.newArrayList();
//                List<JSONObject> resultSon = Lists.newArrayList();
//                for (int j = 0; j < jsonList.size(); j++) {
//                    resultSon.add(jsonList.get(j));
//                    count++;
//                    if (count % 1000 == 0) {
//                        result.add(resultSon);
//                        count = 0;
//                        resultSon = Lists.newArrayList();
//                    }
//                }
//                result.add(resultSon);
//                for (List<JSONObject> list : result) {
//                    myDataMongoDriver.addBatch(myDataBean.getIp(), myDataBean.getPort(), myDataBean.getDbName(), myDataBean.getTbName(), null, list);
//                    successNum += list.size();
//                }
//
//            } catch (Exception e) {
//                failureNum++;
//            } finally {
//
//            }
//
//        }
//        Map<String, Object> retCode = new HashMap<>();
//        retCode.put("successNum", successNum);
//        retCode.put("failureNum", failureNum);
//        if (errorList.size() != 0) {
//            String csvName = myDataBean.getId() + McnUtils.simpleUUID() + ".csv";
//            List<String> schema = new ArrayList<>();
//            JSONArray schemajson = JSON.parseArray(myDataBean.getSchemaConfig());
//            schemajson.forEach(s -> {
//                JSONObject one = (JSONObject) s;
//                schema.add(one.getString("field"));
//            });
//            schema.add("error");
//            try {
//                outPutCsv(errorList, schema, System.getProperty("catalina.base") + file_dir_path + csvName);
//                retCode.put("errorPath", csvName);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return retCode;
//
//    }
//
//    public JSONObject setDateWithSchema(JSONArray schemas, Map<String, Object> data) {
//
//        boolean flag = false;
//        List<String> schemaList = Lists.newArrayList();
//        List<String> dataList = Lists.newArrayList();
//
//        for (int i = 0; i < schemas.size(); i++) {
//            JSONObject temp = schemas.getJSONObject(i);
//            String field = temp.getString("field");
//            schemaList.add(field);
//        }
//
//        for (Map.Entry<String, Object> entry : data.entrySet()) {
//            dataList.add(entry.getKey());
//        }
//
//        for (String str : dataList) {
//            if (schemaList.contains(str)) {
//                flag = true;
//                break;
//            }
//        }
//
//        if (flag == false) {
//            throw RestException.newInstance(ErrorCodes.FIELD_IMPORT_ERROR);
//        }
//
////        if (!schemaList.containsAll(dataList)) {
////            throw RestException.newInstance(ErrorCodes.FIELD_IMPORT_ERROR);
////        }
//
//        JSONObject saveData = new JSONObject();
//        String error = "";
//        for (int i = 0; i < schemas.size(); i++) {
//            JSONObject schema = schemas.getJSONObject(i);
//            String field = schema.getString("field");
//            Integer type = schema.getInteger("type");
//
//            Object fieData = "";
//
//            if (data.get(field) != null) {
////                fieData = data.get(field).toString();
//
//                fieData = data.get(field);
//            } else {
//                fieData = null;
//            }
//            Object objectData = null;
//            switch (type) {
//                case 0:
//                    try {
//                        if (fieData != null) {
//                            if ("".equals(fieData)) {
//                                objectData = null;
//                            } else {
//                                BigDecimal bigDecimal = new BigDecimal((int) fieData);
//                                objectData = bigDecimal.intValue();
//                            }
//                        }
//                    } catch (Exception e) {
//                        try {
//                                objectData = Integer.valueOf(String.valueOf(fieData).split("\\.")[0]);
//                        } catch (NumberFormatException e1) {
//                            error += field + "字段Integer型解析错误,";
//                        }
//                    }
//                    break;
//                case 1:
//                    if (data.get(field) == null) {
//                        objectData = "";
//                    } else {
//                        objectData =  data.get(field).toString().trim();
//                    }
//                    break;
//                case 2:
//                    try {
//                        if (fieData != null) {
//                            if ("".equals(fieData)) {
//                                objectData = 0L;
//                            } else {
//                                BigDecimal bigDecimal = new BigDecimal((long) fieData);
//                                objectData = bigDecimal.longValue();
//                            }
//
//                        }
//                    } catch (Exception e) {
//                        try {
//
//                                objectData = Long.valueOf(String.valueOf(fieData).split("\\.")[0]);
//                        } catch (NumberFormatException e1) {
//                            error += field + "字段Long型解析错误,";
//                        }
//                    }
//
//                    break;
//                case 3:
//
//                    if (fieData == null || "".equals(fieData)) {
//                        objectData = null;
//                        break;
//                    } else if (!Pattern.matches("^((?:19|20)\\d\\d)-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$", String.valueOf(fieData))) {
//                        if (!Pattern.matches("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s((([0-1][0-9])|(2?[0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$", String.valueOf(fieData))) {
//                            error += field + field + "字段日期格式解析错误,";
//                        }
//                    }
//                    objectData = fieData;
//                    break;
//                case 4:
//                    try {
//                        if (!"".equals(data.get(field)) && data.get(field) != null) {
//                            if (fieData != null) {
//                                List<String> objectDatList = Lists.newArrayList();
//                                try {
//                                    objectDatList = JsonUtils.fromJson(String.valueOf(fieData), new TypeToken<List<String>>() {
//                                    }.getType());
//                                } catch (Exception e) {
//                                    try {
//                                        objectData = JSON.toJSON(fieData);
//
//                                    } catch (Exception e2) {
//                                        try {
//                                            objectData = JSONArray.parse(String.valueOf(fieData));
//                                        } catch (Exception e1) {
//                                            error += field + field + "字段Array解析错误,";
//                                        }
//
//                                    }
//                                }
//                                if (objectDatList.size() > 0) {
//                                    throw RestException.newInstance(ErrorCodes.STRING_ARRAY_FORMAT_ERROR2);
//                                }
//                            }
//                        }
//                    } catch (Exception e) {
////                        error += field + field + "字段object解析错误,";
//                    }
//                    break;
//                case 5:
//                    try {
//                        objectData = data.get(field);
//                    } catch (Exception e) {
//                        error += field + field + "字段nested解析错误,";
//                    }
//                    break;
//                case 6:
//
//                    if (data.get(field) != null) {
//                        if (fieData != null) {
//                            try {
//                                String jsData = JsonUtils.toJson(data.get(field));
//                                List<String> objectDatList = JsonUtils.fromJson(JsonUtils.toJson(data.get(field)), new TypeToken<List<JSONObject>>() {
//                                }.getType());
//                                objectData = objectDatList;
//                            } catch (Exception e2) {
//                                try {
//                                    List<JSONObject> objectDatList = JsonUtils.fromJson(String.valueOf(fieData), new TypeToken<List<JSONObject>>() {
//                                    }.getType());
//                                    objectData = objectDatList;
//                                } catch (Exception e) {
//                                    throw RestException.newInstance(ErrorCodes.OBJECT_ARRAY_FORMAT_ERROR2);
//                                }
//
//                            }
//                        }
//                    }
//
//                    break;
//                case 7:
//                    if (data.get(field) != null) {
//                        try {
//                            if (fieData != null) {
//                                List<String> objectDatList = JsonUtils.fromJson(String.valueOf(fieData), new TypeToken<List<String>>() {
//                                }.getType());
//                                objectData = objectDatList;
//                            }
//
//                        } catch (Exception e) {
//                            throw RestException.newInstance(ErrorCodes.STRING_ARRAY_FORMAT_ERROR);
//                        }
//                    }
//                    break;
//                case 8:
//                    try {
//                        if (fieData != null) {
//                            if ("".equals(fieData)) {
//                                objectData = null;
//                            } else {
//                                objectData = Double.valueOf(String.valueOf(fieData));
//                            }
//                        }
//                    } catch (NumberFormatException e) {
//                        error += field + "字段Double型解析错误,";
//                    }
//
//
//                    break;
//                case 9:
//                    try {
//                        if (fieData != null) {
//                            if ("".equals(fieData)) {
//                                objectData = null;
//                            } else {
//                                objectData = Double.valueOf(String.valueOf(fieData));
//                            }
//                        }
//                    } catch (NumberFormatException e) {
//                        error += field + "字段Float型解析错误,";
//                    }
//                    break;
//                default:
//                    break;
//            }
//            saveData.put(field, objectData);
//        }
//
//        if (!"".equals(error)) {
//            saveData.put("_error_code", error.substring(0, error.length() - 1));
//        }
//        return saveData;
//    }
//
//    @Override
//    public Map<String, Object> interfaceUpload(String data, String dataName, String apk) {
//        //验证apk是否正确
//        String userId = userApkBeanMapper.getUserIdByApk(apk);
//        if (userId == null) {
//            throw RestException.newInstance(ErrorCodes.APK_NULL_ERROR);
//        }
//
//        //查询对应的数据集信息
//        Map<String, Object> idMap = new HashMap<>();
//        idMap.put("dataName", dataName);
//        idMap.put("userId", userId);
//        MyDataBean myDataBean = myDataMapper.selectByDataName(idMap);
//
//        if (myDataBean == null || (myDataBean.getDataType() != 2 && myDataBean.getDataType() != 1)) {
//            throw ServiceException.newInstance(ErrorCodes.MYDATA_NOTEXIST_ERROR);
//        }
//
//
//        //请求接口
//         List<JSONObject> map = Lists.newArrayList();
//        try {
//            map = JsonUtils.fromJson(data, new TypeToken<List<JSONObject>>() {
//            }.getType());
//        } catch (Exception e) {
//            try {
//                JSONObject jsonObject = JsonUtils.fromJson(data, JSONObject.class);
//                map.add(jsonObject);
//            } catch (Exception e1) {
//                throw RestException.newInstance(ErrorCodes.DATA_FORMAT_ERROR);
//            }
//
//        }
//
//        int successNum = 0;
//        int failureNum = 0;
//        int i = 0;
//        List<Map<String, Object>> errorList = new ArrayList<>();
//
//        StringBuffer query = new StringBuffer();
//        RestClient restClient = null;
//        NStringEntity entity = null;
//        String table = "{ \"index\" : { \"_index\" : \"" + myDataBean.getDbName() + "\", \"_type\" : \"" + myDataBean.getTbName() + "\" } }";
//
//
//        if (myDataBean.getDataType() == 2) {
//
//            for (Map<String, Object> one : map) {
//                i++;
//                JSONObject saveData = setDateWithSchema(JSONArray.parseArray(myDataBean.getSchemaConfig()), one);
//                if (saveData.containsKey("_error_code")) {
//                    one.put("error", saveData.getString("_error_code"));
//                    errorList.add(one);
//                    continue;
//                }
//
//                //添加检测字段
//                setTimeField(saveData);
//
//                query.append(table + "\n");
//                query.append(saveData.toString() + "\n");
//
//
//                if (i % 1000 == 0) {
//                    i++;
//                    try {
//                        restClient = EsRestClient.getClient(myDataBean.getIp(), configManage.getCloudEsRestPort());
//                        String endpoint = "/_bulk";
//                        entity = new NStringEntity(query.toString(), ContentType.APPLICATION_JSON);
//                        Response response = restClient.performRequest("POST", endpoint, Collections.singletonMap("pretty", "true"), entity);
//                        String rs = EntityUtils.toString(response.getEntity());
//                        successNum++;
//
//                    } catch (Exception e) {
//                        failureNum++;
//                        throw RestException.newInstance(ErrorCodes.DATA_FORMAT_ERROR);
//                    } finally {
//                        if (entity != null) {
//                            entity.close();
//                        }
//                        if (Objects.nonNull(restClient)) {
//                            try {
//                                restClient.close();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                    query = new StringBuffer();
//                }
//            }
//            try {
//                restClient = EsRestClient.getClient(myDataBean.getIp(), configManage.getCloudEsRestPort());
//                String endpoint = "/_bulk";
//                entity = new NStringEntity(query.toString(), ContentType.APPLICATION_JSON);
//
//                Response response = restClient.performRequest("POST", endpoint, Collections.singletonMap("pretty", "true"), entity);
//                String rs = EntityUtils.toString(response.getEntity());
//
//                successNum++;
//
//            } catch (Exception e) {
//
//                failureNum++;
//                throw RestException.newInstance(ErrorCodes.DATA_FORMAT_ERROR);
//            } finally {
//                if (entity != null) {
//                    entity.close();
//                }
//                if (Objects.nonNull(restClient)) {
//                    try {
//                        restClient.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//        } else if (myDataBean.getDataType() == 1) {
//            List<JSONObject> jsonList = Lists.newArrayList();
//            for (Map<String, Object> one : map) {
//                JSONObject saveData = setDateWithSchema(JSONArray.parseArray(myDataBean.getSchemaConfig()), one);
//                setTimeField(saveData);
//                if (saveData.containsKey("_error_code")) {
//                    continue;
//                }
//
//                jsonList.add(saveData);
//            }
//
//            try {
//
//                int count = 0;
//                List<List<JSONObject>> result = Lists.newArrayList();
//                List<JSONObject> resultSon = Lists.newArrayList();
//                for (int j = 0; j < jsonList.size(); j++) {
//                    resultSon.add(jsonList.get(j));
//                    count++;
//                    if (count % 1000 == 0) {
//                        result.add(resultSon);
//                        count = 0;
//                        resultSon = Lists.newArrayList();
//                    }
//                }
//                result.add(resultSon);
//                for (List<JSONObject> list : result) {
//                    myDataMongoDriver.addBatch(myDataBean.getIp(), myDataBean.getPort(), myDataBean.getDbName(), myDataBean.getTbName(), null, list);
//                    successNum += list.size();
//                }
//
//            } catch (Exception e) {
//                failureNum++;
//            } finally {
//
//            }
//        }
//        Map<String, Object> retCode = new HashMap<>();
//        retCode.put("successNum", successNum);
//        retCode.put("failureNum", failureNum);
//        if (errorList.size() != 0) {
//            String csvName = myDataBean.getId() + McnUtils.simpleUUID() + ".csv";
//            List<String> schema = new ArrayList<>();
//            JSONArray schemajson = JSON.parseArray(myDataBean.getSchemaConfig());
//            schemajson.forEach(s -> {
//                JSONObject one = (JSONObject) s;
//                schema.add(one.getString("field"));
//            });
//            schema.add("error");
//            try {
//                outPutCsv(errorList, schema, System.getProperty("catalina.base") + file_dir_path + csvName);
//                retCode.put("errorPath", csvName);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return retCode;
//    }
//
//    private void setTimeField(JSONObject jsonObject){
//        jsonObject.put("_persistTime", CommonUtils.formatCurrentDate());
//        jsonObject.put("_oprTime", CommonUtils.formatCurrentDate());
//        jsonObject.put("oprTime", CommonUtils.formatCurrentDate());
//    }
//
//    private void outPutCsv(List<Map<String, Object>> data, List<String> schema, String pathname) throws IOException {
//
//        try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(new File(pathname)), "gbk");CSVWriter writer = new CSVWriter(osw)) {
//
//            String[] schemaArr = new String[schema.size()];
//            schemaArr = schema.toArray(schemaArr);
//            writer.writeNext(schemaArr);
//            CSVWriter finalWriter = writer;
//            data.forEach(s -> {
//                String[] entries = new String[schema.size()];
//                for (int i = 0; i < schema.size(); i++) {
//                    entries[i] = s.get(schema.get(i)).toString();
//                }
//                finalWriter.writeNext(entries);
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


}
