package com.plantdata.kgcloud.domain.prebuilder.aop;


import ai.plantdata.cloud.bean.BaseReq;

/**
 * @author Bovin
 * @description
 * @since 2020-05-28 16:50
 **/
public class BaseHandlerReq extends BaseReq implements HandlerReq {

    private Object response;

    @Override
    public Object getResponse() {
        return response;
    }

    @Override
    public void setResponse(Object response) {
        this.response = response;
    }
}
