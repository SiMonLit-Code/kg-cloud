package com.plantdata.kgcloud.domain.common.service;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.constant.KgDocumentErrorCodes;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.AppClient;
import com.plantdata.kgcloud.sdk.req.app.InfoBoxReq;
import com.plantdata.kgcloud.sdk.req.app.PromptReq;
import com.plantdata.kgcloud.sdk.rsp.app.main.InfoBoxRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.PromptEntityRsp;
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

}
