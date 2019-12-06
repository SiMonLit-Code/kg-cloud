package com.plantdata.kgcloud.domain.scene.service;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.scene.entiy.Scene;
import com.plantdata.kgcloud.domain.scene.req.SceneQueryReq;
import com.plantdata.kgcloud.domain.scene.req.SceneReq;
import com.plantdata.kgcloud.domain.scene.rsp.SceneRsp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SceneService {
    ApiReturn<Page<SceneRsp>> findAll(SceneQueryReq sceneQueryReq, Pageable pageable);

    ApiReturn<SceneRsp> getSceneDetail(Integer id);

    ApiReturn<SceneRsp> createScene(SceneReq sceneReq);

    Scene checkScene(Integer id, String userId);
}
