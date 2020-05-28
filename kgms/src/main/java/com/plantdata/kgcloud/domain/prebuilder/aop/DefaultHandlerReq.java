package com.plantdata.kgcloud.domain.prebuilder.aop;

/**
 * @author Bovin
 * @description
 * @since 2020-05-28 16:44
 **/
public class DefaultHandlerReq implements HandlerReq {

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
