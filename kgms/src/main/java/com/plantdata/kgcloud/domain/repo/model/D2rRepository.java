package com.plantdata.kgcloud.domain.repo.model;

import com.plantdata.kgcloud.domain.repo.enums.RepoCheckType;
import com.plantdata.kgcloud.domain.repo.model.req.DealDTO;
import lombok.Getter;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.List;
import java.util.function.Function;

/**
 * @author cjw
 * @date 2020/5/15  14:19
 */

public class D2rRepository implements RepositoryRoot, ConsulService {

    private final DiscoveryClient discoveryClient;
    private List<RepositoryHandler> handlers;
    public Function<DealDTO, DealDTO> d2rFunction;

    private DealDTO basicReq;

    public D2rRepository(DiscoveryClient discoveryClient, List<RepositoryHandler> handlers, Function<DealDTO, DealDTO> d2rFunction, DealDTO bji) {
        this.discoveryClient = discoveryClient;
        this.handlers = handlers;
        this.d2rFunction = d2rFunction;
        this.basicReq = bji;
    }

    @Override
    public RepoCheckType checkType() {
        return RepoCheckType.CONSUL;
    }

    @Override
    public DealDTO getBasicReq() {
        return basicReq;
    }

    @Override
    public Function<DealDTO, DealDTO> BasicRequest() {
        return d2rFunction;
    }

    @Override
    public List<RepositoryHandler> handlers() {
        return handlers;
    }

    @Override
    public DiscoveryClient discoveryClient() {
        return discoveryClient;
    }
}
