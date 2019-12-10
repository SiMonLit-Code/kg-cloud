package com.plantdata.kgcloud.domain.d2r.service.ipml;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.mongodb.client.MongoCursor;
import com.plantdata.kgcloud.common.util.MongoUtil;
import com.plantdata.kgcloud.constant.CommonErrorCode;
import com.plantdata.kgcloud.domain.d2r.entity.*;
import com.plantdata.kgcloud.domain.d2r.service.D2rService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.KgmsClient;
import com.plantdata.kgcloud.sdk.rsp.DataSetRsp;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * @author xiezhenxiang 2019/12/9
 */
@Service
public class D2rServiceImpl implements D2rService {

    @Autowired
    private KgmsClient kgmsClient;

    @Override
    public PreviewRsp preview(PreviewReq req) {

        req.setEntSize(Math.min(req.getEntSize(), 100));
        req.setSynonymSize(Math.min(req.getSynonymSize(), 100));
        req.setAttrSize(Math.min(req.getAttrSize(), 100));
        req.setRelSize(Math.min(req.getRelSize(), 100));
        List<D2RMapperConfigBean> configBeanList = null;
        configBeanList = parse(req.getKgName(), D2RDataType.ENTITY, req.getSetting());
        if (configBeanList == null || configBeanList.size() <= 0) {
            return null;
        }
        PreviewRsp bean = new PreviewRsp();
        List<EntRsp> list = new ArrayList<>();
        List<AttrRsp> attrs = new ArrayList<>();
        List<SynonymRsp> synonymBeans = new ArrayList<>();
        int entitySize = 0, synSize = 0, attrSize = 0;

        for (D2RMapperConfigBean configBean : configBeanList) {
            int pageNo = 1;
            int pageSize = req.getEntSize() > 0 ? req.getEntSize() : 10;
            JSONArray datas = this.loadData(configBean.getDataSetId(), pageNo, pageSize);
            int dataLength = datas.size();
            for (int i = 0; i < dataLength; i++) {
                JSONObject data = datas.getJSONObject(i);
                String name = "";
                long eid = i;
                long cid = configBean.getConceptId();
                for (EntMapperElementBean en : configBean.getEnt()) {
                    if (en.getProperty().equals("name")) {
                        for (String k : en.getMapField()) {
                            if (!StringUtils.isEmpty(data.getString(k))) {
                                name += data.getString(k);
                            }
                        }
                    }
                }
                // 添加实体
                if (entitySize < req.getEntSize()) {
                    EntRsp entBean = new EntRsp();
                    entBean.setName(name);
                    entBean.setCid(cid);
                    entBean.setEntId(eid);
                    list.add(entBean);
                    entitySize ++;
                }

                if (attrSize < req.getAttrSize() && configBean.getAttrs() != null) {
                    for (AttrMapperElementBean attrMapper : configBean.getAttrs()) {
                        AttrRsp attr = new AttrRsp();
                        attr.setCid(cid);
                        attr.setAttrId(attrMapper.getAttrDef());
                        String attrv = "";
                        for (String k : attrMapper.getMapField()) {
                            attrv += data.getString(k) + ";";
                        }
                        attrv = attrv.substring(0, attrv.length() - 1);
                        attr.setAttrValue(attrv);
                        attr.setName(name);
                        attr.setEntId(eid);
                        attrs.add(attr);
                        attrSize ++;
                    }
                }
                // 私有属性
                if (attrSize < req.getAttrSize() && configBean.getPrivateAttrs() != null) {
                    for (PrivateAttrBean privateAttrBean : configBean.getPrivateAttrs()) {
                        AttrRsp attr = new AttrRsp();
                        String attrName = privateAttrBean.getKeyIsConstant() == 1 ? privateAttrBean.getKeyField() :
                                data.get(privateAttrBean.getKeyField()) + "";
                        attr.setCid(cid);
                        attr.setAttrName(attrName);
                        String attrValue = data.get(privateAttrBean.getValueField()) + "";
                        attr.setAttrValue(attrValue);
                        attr.setName(name);
                        attr.setEntId(eid);
                        attrs.add(attr);
                        attrSize++;
                    }
                }
                if (synSize < req.getSynonymSize() && configBean.getSynonyms() != null) {
                    for (String k : configBean.getSynonyms().getMapField()) {
                        SynonymRsp synonymBean = new SynonymRsp();
                        synonymBean.setName(name);
                        synonymBean.setSynonym(data.getString(k));
                        synonymBean.setUpdateOpt(configBean.getSynonyms().getUpdateOpt());
                        synonymBean.setCid(cid);
                        synonymBean.setEntId(eid);
                        synonymBeans.add(synonymBean);
                        synSize++;
                    }
                }
                if (entitySize >= req.getEntSize()) {
                    break;
                }
            }
            if (entitySize >= req.getEntSize()) {
                break;
            }
        }
        bean.setEnts(list.size() > 0 ? list : null);
        bean.setAttrs(attrs.size() > 0 ? attrs : null);
        bean.setSynonyms(synonymBeans.size() > 0 ? synonymBeans : null);
        return bean;
    }

    private List<D2RMapperConfigBean> parse(String kgName, D2RDataType dataType, String config) {

        List<D2RMapperConfigBean> configBeanList = null;
        try {
            configBeanList = JacksonUtils.getInstance().readValue(config, new TypeReference<List<D2RMapperConfigBean>>(){});
        } catch (IOException e) {
            throw BizException.of(CommonErrorCode.BAD_REQUEST);
        }

        for (D2RMapperConfigBean configBean : configBeanList) {
            configBean.setUserId("");
            configBean.setKgName(kgName);
            configBean.setDataType(dataType);
            if (dataType == D2RDataType.ENTITY) {
                for (MapperElementBean mapperEle : configBean.getEnt()) {
                    mapperEle.setUpdateOpt(configBean.getUpdateOpt());
                }
            } else if(dataType == D2RDataType.SYNONYM) {
                configBean.getSynonyms().setUpdateOpt(configBean.getUpdateOpt());
            } else if(dataType == D2RDataType.RELATION) {
                for (MapperElementBean mapperEle : configBean.getRels()) {
                    mapperEle.setUpdateOpt(configBean.getUpdateOpt());
                }
            }
        }
        return configBeanList;
    }

    private JSONArray loadData(String dataSetId, int page, int size) {

        DataSetRsp  dataSetRsp = kgmsClient.dataSetFindById(Long.parseLong(dataSetId)).getData();
        MongoUtil mongoUtil = new MongoUtil(dataSetRsp.getAddr());
        MongoCursor<Document> cursor = mongoUtil.find(dataSetRsp.getDbName(), dataSetRsp.getTbName(), null, null, page, size);

        JSONArray ls = new JSONArray();
        cursor.forEachRemaining(s -> {
            s.remove("_id");
            for (Map.Entry<String, Object> entry : s.entrySet()) {
                if (entry.getValue() != null && entry.getValue() instanceof Date) {
                    String dateValue = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(entry.getValue());
                    s.append(entry.getKey(), dateValue);
                }
            }
            ls.add(JSONObject.parseObject(JSON.toJSONString(s)));
        });
        return ls;
    }
}
