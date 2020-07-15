package ai.plantdata.kgcloud.domain.prebuilder.aop;

/**
 * @author Bovin
 * @description
 * @since 2020-05-28 16:40
 **/
public interface HandlerReq {

    Object getResponse();

    void setResponse(Object response);
}
