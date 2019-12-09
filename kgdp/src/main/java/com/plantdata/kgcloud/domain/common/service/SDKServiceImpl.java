package com.plantdata.kgcloud.domain.common.service;

import com.google.common.collect.Lists;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.constant.KgDocumentErrorCodes;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.AppClient;
import com.plantdata.kgcloud.sdk.EditClient;
import com.plantdata.kgcloud.sdk.req.app.InfoBoxReq;
import com.plantdata.kgcloud.sdk.req.app.PromptReq;
import com.plantdata.kgcloud.sdk.rsp.app.OpenBatchSaveEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.InfoBoxRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.PromptEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.edit.BatchRelationRsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SDKServiceImpl implements SDKService {


    @Autowired
    private AppClient appClient;

    @Autowired
    private EditClient editClient;

    @Override
    public Long getEntityIdByName(String kgName, String name, Long conceptId) {

        PromptReq promptReq = new PromptReq();
        promptReq.getConceptIds().add(conceptId);
        promptReq.setKw(name);

        ApiReturn<List<PromptEntityRsp>> apiReturn = appClient.prompt(kgName,promptReq);

        if(apiReturn.getErrCode() != 200){
            throw BizException.of(KgDocumentErrorCodes.HTTP_ERROR);
        }

        List<PromptEntityRsp> promptEntityRspList = apiReturn.getData();

        if(Objects.nonNull(promptEntityRspList) && !promptEntityRspList.isEmpty()){
            return promptEntityRspList.get(0).getId();
        }

        return null;
    }

    @Override
    public InfoBoxRsp infobox(String kgName, Long entityId) {
        InfoBoxReq infoBoxReq = new InfoBoxReq();
        infoBoxReq.setEntityIdList(Stream.of(entityId).collect(Collectors.toList()));


        ApiReturn<List<InfoBoxRsp>> apiReturn = appClient.infoBox(kgName,infoBoxReq);
        if(apiReturn.getErrCode() != 200){
            throw BizException.of(KgDocumentErrorCodes.HTTP_ERROR);
        }

        List<InfoBoxRsp> infoBoxRspList = apiReturn.getData();
        if(Objects.nonNull(infoBoxReq) && !infoBoxRspList.isEmpty()){
            return infoBoxRspList.get(0);
        }

        return null;

    }

    @Override
    public List<BatchRelationRsp> addBatchRelation(String kgName, List<BatchRelationRsp> relationList) {

        ApiReturn<BatchRelationRsp> apiReturn = editClient.importRelation(kgName,relationList.get(0));
        if(apiReturn.getErrCode() != 200){
            throw BizException.of(KgDocumentErrorCodes.HTTP_ERROR);
        }

        BatchRelationRsp relationRsp = apiReturn.getData();
        if(Objects.nonNull(relationRsp)){
            return Lists.newArrayList(relationRsp);
        }

        return null;
    }

    @Override
    public List<OpenBatchSaveEntityRsp> addBatchEntity(String kgName, List<OpenBatchSaveEntityRsp> entityList) {
        ApiReturn<List<OpenBatchSaveEntityRsp>> apiReturn = editClient.saveOrUpdate(kgName,true,entityList);
        if(apiReturn.getErrCode() != 200){
            throw BizException.of(KgDocumentErrorCodes.HTTP_ERROR);
        }

        List<OpenBatchSaveEntityRsp> entityRspList = apiReturn.getData();
        if(Objects.nonNull(entityRspList) && !entityRspList.isEmpty()){
            return entityRspList;
        }

        return null;
    }

}
