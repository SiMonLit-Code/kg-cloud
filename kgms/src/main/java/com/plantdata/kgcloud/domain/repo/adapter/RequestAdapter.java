package com.plantdata.kgcloud.domain.repo.adapter;

import com.plantdata.kgcloud.domain.repo.model.req.D2rReq;
import com.plantdata.kgcloud.domain.repo.model.req.DealDTO;
import com.plantdata.kgcloud.domain.repo.model.rsp.D2rRsp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author cjw
 * @date 2020/5/15  18:00
 */
@Service
@Slf4j
public class RequestAdapter {

    /**
     * d2r基础方法
     *
     * @param dealDTO
     * @param <T>
     * @return
     */
    public <T extends DealDTO> T d2RBaSic(T dealDTO) {
        assert dealDTO instanceof D2rDTO;
        return dealDTO;
    }

    /**
     * 前置处理
     *
     * @param dealDTO
     * @param <T>
     * @return
     */
    public <T extends DealDTO> T d2RPreRequest(T dealDTO) {
        assert dealDTO instanceof D2rDTO;
        ((D2rDTO) dealDTO).name = "前置处理";
        return dealDTO;
    }

    /**
     * 后置处理
     *
     * @param dealDTO
     * @param <T>
     * @return
     */
    public <T extends DealDTO> T d2RPostRequest(T dealDTO) {
        assert dealDTO instanceof D2rDTO;
        ((D2rDTO) dealDTO).rsp = new D2rRsp();
        return dealDTO;
    }


    public static class D2rDTO implements DealDTO {
        private D2rRsp rsp;
        private String name;
        private D2rReq d2rReq;

        public D2rDTO(D2rReq d2rReq) {
            this.d2rReq = d2rReq;
        }

        @Override
        public Object rsp() {
            return rsp;
        }
    }
}
