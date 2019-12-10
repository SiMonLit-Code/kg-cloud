package com.plantdata.kgcloud.domain.parse.service;

import cn.hiboot.mcn.core.model.result.RestResp;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hiekn.pddocument.bean.PdDocument;
import com.hiekn.pddocument.bean.element.PdEntity;
import com.hiekn.pddocument.bean.element.PdRelation;
import com.hiekn.pddocument.bean.element.PdSegment;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.constant.KgDocumentErrorCodes;
import com.plantdata.kgcloud.domain.document.rsp.PddocumentRsp;
import com.plantdata.kgcloud.domain.parse.concerter.RestRespConverter;
import com.plantdata.kgcloud.domain.parse.req.NlpParseReq;
import com.plantdata.kgcloud.domain.parse.util.ParseCountryUtils;
import com.plantdata.kgcloud.domain.scene.entiy.Scene;
import com.plantdata.kgcloud.domain.scene.rsp.NlpRsp;
import com.plantdata.kgcloud.domain.scene.service.SceneService;
import com.plantdata.kgcloud.exception.BizException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ParseServiceImpl implements ParseService {

    @Autowired
    private SceneService sceneService;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ApiReturn<PdDocument> parseCountry(MultipartFile multiRequest) {

        try {
            String fileName = multiRequest.getOriginalFilename();
            InputStream in = multiRequest.getInputStream();
            return ApiReturn.success(ParseCountryUtils.getReplaceElementsInWord(fileName,in));
        }catch (IOException e){
            throw BizException.of(KgDocumentErrorCodes.DOCUMENT_PARSE_ERROR);
        }
    }

    @Override
    public ApiReturn<List<PdDocument>> nlpParse(NlpParseReq nlpParseReq) {

        List<NlpRsp> nlpConfigList = nlpParseReq.getNlpConfigs();
        List<String> inputs = nlpParseReq.getInputs();

        if(Objects.isNull(nlpConfigList) || nlpConfigList.isEmpty()){
            return ApiReturn.success(Lists.newArrayList());
        }

        if(Objects.isNull(inputs) || inputs.isEmpty()){
            return ApiReturn.success(Lists.newArrayList());
        }

        List<PdDocument> pdDocumentList = parseDefaultPdDocument(nlpParseReq.getInputs());

        for(NlpRsp nlpRsp : nlpConfigList){

            String url = nlpRsp.getModelApi();
            List<PdDocument> pdDocuments = getNlpResult(url, nlpParseReq.getInputs());
            mergePdDocuments(pdDocumentList, pdDocuments);
        }

        return ApiReturn.success(pdDocumentList);
    }

    private void mergePdDocuments(List<PdDocument> master, List<PdDocument> branch) {

        if(Objects.isNull(branch) || branch.isEmpty()){
            return;
        }

        if(master.size() != branch.size()){
            return ;
        }

        //实体抽取
        for (int i = 0; i < master.size(); i++) {
            List<PdEntity> oneEntitys = master.get(i).getPdEntity() == null ? Lists.newArrayList() : master.get(i).getPdEntity();
            List<PdEntity> authEntitys = branch.get(i).getPdEntity() == null ? Lists.newArrayList() : branch.get(i).getPdEntity();

            List<String> ignoreEntitys = Lists.newArrayList();

            for (PdEntity authEntity : authEntitys) {
                boolean isMatch = false;
                Integer authIndex = authEntity.getIndex();
                Integer authEnd = authEntity.getName().length() + authIndex;
                for (PdEntity oneEntity : oneEntitys) {
                    Integer index = oneEntity.getIndex();
                    Integer end = index + oneEntity.getName().length();
                    if ((authIndex > index && authIndex < end) || (authEnd > index && authEnd < end)) {
                        isMatch = true;
                        break;
                    }

                }
                if (!isMatch) {
                    oneEntitys.add(authEntity);
                }else{
                    ignoreEntitys.add(authEntity.getIndex()+"_"+authEntity.getName());
                }
            }
            master.get(i).setPdEntity(oneEntitys);



            List<PdRelation> oneRelations = master.get(i).getPdRelation() == null ? Lists.newArrayList(): master.get(i).getPdRelation();
            List<PdRelation> authRelations = branch.get(i).getPdRelation() == null ? Lists.newArrayList(): branch.get(i).getPdRelation();

            for(PdRelation pdRelation : authRelations){

                String subjectKey = pdRelation.getSubject().getIndex()+"_"+pdRelation.getSubject().getName();
                String objectKey = pdRelation.getObject().getIndex()+"_"+pdRelation.getObject().getName();
                if(ignoreEntitys.contains(subjectKey) || ignoreEntitys.contains(objectKey)){
                    continue;
                }

                Boolean isMatch = false;
                for(PdRelation oneRelation : oneRelations){
                    if(oneRelation.getSubject().getName().equals(pdRelation.getSubject().getName())
                    && oneRelation.getSubject().getIndex().equals(pdRelation.getSubject().getIndex())
                    && oneRelation.getObject().getName().equals(pdRelation.getObject().getName())
                    && oneRelation.getObject().getIndex().equals(pdRelation.getObject().getIndex())
                    && oneRelation.getAttName().equals(pdRelation.getAttName())){
                        isMatch = true;
                        break;
                    }
                }

                if(!isMatch){
                    oneRelations.add(pdRelation);
                }

            }

            master.get(i).setPdRelation(oneRelations);
        }

    }

    private List<PdDocument> getNlpResult(String url, List<String> inputs) {


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> postParameters = new LinkedMultiValueMap<>();
        postParameters.add("input", JSON.toJSONString(inputs));

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(postParameters, headers);

        String rs;
        try {
            rs = restTemplate.postForObject(url, requestEntity, String.class);
        }catch (Exception e){
            throw BizException.of(KgDocumentErrorCodes.HTTP_ERROR);
        }

        RestResp<List<PdDocument>> restResp = new Gson().fromJson(rs, new TypeToken<RestResp<List<PdDocument>>>(){}.getType());

        Optional<List<PdDocument>> optional = RestRespConverter.convert(restResp);

        return optional.get();

    }

    private List<PdDocument> parseDefaultPdDocument(List<String> inputs){
        if(Objects.isNull(inputs) || inputs.isEmpty()){
            return Lists.newArrayList();
        }

        List<PdDocument> rsList =  inputs.stream().map(input -> {
            PdDocument pdDocument = new PdDocument();

            pdDocument.setContent(input);
            pdDocument.setPdEntity(Lists.newArrayList());
            pdDocument.setPdRelation(Lists.newArrayList());
            return pdDocument;
        }).collect(Collectors.toList());

        return rsList;
    }
}
