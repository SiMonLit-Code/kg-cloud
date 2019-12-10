package com.plantdata.kgcloud.domain.scene.service;

import com.google.common.collect.Maps;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.constant.KgDocumentErrorCodes;
import com.plantdata.kgcloud.domain.scene.entiy.Scene;
import com.plantdata.kgcloud.domain.scene.repository.SceneRepository;
import com.plantdata.kgcloud.domain.scene.req.SceneQueryReq;
import com.plantdata.kgcloud.domain.scene.req.SceneReq;
import com.plantdata.kgcloud.domain.scene.rsp.SceneRsp;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.rsp.app.main.AttributeDefinitionRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.BaseConceptRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.SchemaRsp;
import com.plantdata.kgcloud.security.SessionHolder;
import com.plantdata.kgcloud.util.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SceneServiceImpl implements SceneService {

    @Autowired
    private SceneRepository sceneRepository;

    @Override
    public ApiReturn<Page<SceneRsp>> findAll(SceneQueryReq sceneQueryReq, Pageable pageable) {

        String userId = SessionHolder.getUserId();

        Scene scene = Scene.builder().userId(userId).build();

        Page<Scene> all = sceneRepository.findAll(Example.of(scene),pageable);
        Page<SceneRsp> map = all.map(ConvertUtils.convert(SceneRsp.class));

        return ApiReturn.success(map);
    }

    @Override
    public ApiReturn<SceneRsp> getSceneDetail(Integer id) {

        String userId = SessionHolder.getUserId();

        Scene scene = Scene.builder().userId(userId).id(id).build();

        Optional<Scene> optional = sceneRepository.findOne(Example.of(scene));

        if(optional == null || !optional.isPresent()){
            throw BizException.of(KgDocumentErrorCodes.SCENE_NOT_EXISTS);
        }

        SceneRsp rsp = ConvertUtils.convert(SceneRsp.class).apply(optional.get());
        return ApiReturn.success(rsp);
    }

    @Override
    public ApiReturn<SceneRsp> createScene(SceneReq sceneReq) {

        String userId = SessionHolder.getUserId();

        Scene scene = ConvertUtils.convert(Scene.class).apply(sceneReq);
        scene.setUserId(userId);

        setSchemaId(scene);

        sceneRepository.save(scene);
        SceneRsp sceneRsp = ConvertUtils.convert(SceneRsp.class).apply(scene);
        return ApiReturn.success(sceneRsp);
    }

    @Override
    public Scene checkScene(Integer id, String userId) {

        Scene scene = Scene.builder().userId(userId).id(id).build();

        Optional<Scene> optional = sceneRepository.findOne(Example.of(scene));

        if(optional == null || !optional.isPresent()){
            throw BizException.of(KgDocumentErrorCodes.SCENE_NOT_EXISTS);
        }
        return optional.get();
    }

    /**
     * 把新建时前端生成的随机id由long转为integer类型
     * @param scene
     */
    public void setSchemaId(Scene scene){

        SchemaRsp schemaRsp = scene.getLabelModel();
        Map<Long,Integer> typeIds = Maps.newHashMap();


        List<BaseConceptRsp> conceptRsps = schemaRsp.getTypes();
        if(conceptRsps != null && !conceptRsps.isEmpty()){

            for (BaseConceptRsp concept : conceptRsps) {
                if(!typeIds.containsKey(concept.getId())){
                    typeIds.put(concept.getId(),typeIds.size()+1);
                }
            }

            for(BaseConceptRsp concept : conceptRsps) {
                Integer conceptId = typeIds.get(concept.getId());
                concept.setId(conceptId.longValue());
            }
        }

        Map<Integer,Integer> attIds = Maps.newHashMap();

        List<AttributeDefinitionRsp> attrs = schemaRsp.getAttrs();
        if(attrs != null && !attrs.isEmpty()){

            for (AttributeDefinitionRsp attr : attrs) {
                if(!attIds.containsKey(attr.getId())){
                    attIds.put(attr.getId(),attIds.size()+1);
                }
            }

            for (AttributeDefinitionRsp attr : attrs) {
                Integer attId = attIds.get(attr.getId());
                attr.setId(attId);
                Integer domain = typeIds.get(attr.getDomainValue());
                attr.setDomainValue(domain.longValue());

                if(attr.getRangeValue() != null && !attr.getRangeValue().isEmpty()){
                    List<Long> rangeList = new ArrayList<>(attr.getRangeValue().size());
                    for(Long range : attr.getRangeValue()){
                        Integer rangeId = typeIds.get(range);
                        rangeList.add(rangeId.longValue());
                    }

                    attr.setRangeValue(rangeList);
                }
            }
        }

    }
}
