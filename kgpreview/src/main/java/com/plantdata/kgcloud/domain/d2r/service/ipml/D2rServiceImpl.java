package com.plantdata.kgcloud.domain.d2r.service.ipml;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.bean.BasePage;
import com.plantdata.kgcloud.constant.CommonErrorCode;
import com.plantdata.kgcloud.domain.d2r.entity.*;
import com.plantdata.kgcloud.domain.d2r.service.D2rService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.KgmsClient;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            System.out.println("data: " + datas.toJSONString());
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
                                name += data.getString(k) + " ";
                            }
                        }
                    }
                }
                if (name.isEmpty()) {
                    continue;
                }
                name = name.substring(0, name.length() - 1);
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

        List<D2RMapperConfigBean> configBeanList;
        try {
            configBeanList = JSONArray.parseArray(config, D2RMapperConfigBean.class);
        } catch (Exception e) {
            e.printStackTrace();
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

        ApiReturn<BasePage<Map<String, Object>>> apiReturn = kgmsClient.dataOptFindAll(Long.parseLong(dataSetId), page, size);
        JSONArray ls = new JSONArray();
        for (Map<String, Object> data : apiReturn.getData().getContent()) {
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                if (entry.getValue() != null && entry.getValue() instanceof Date) {
                    String dateValue = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(entry.getValue());
                    data.put(entry.getKey(), dateValue);
                }
            }
            ls.add(JSONObject.parseObject(JSON.toJSONString(data)));
        }
        return ls;
    }
}
