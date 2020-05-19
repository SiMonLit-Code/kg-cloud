package com.plantdata.kgcloud.domain.repo.checker;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

/**
 * @author cjw
 * @date 2020/5/15  11:12
 */
@Slf4j
public class MongoServiceChecker implements ServiceChecker {

    private Function<String, Boolean> graphExistFunction;
    private String graphName;

    public MongoServiceChecker(Function<String, Boolean> graphExistFunction, String graphName) {
        this.graphExistFunction = graphExistFunction;
        this.graphName = graphName;
    }

    @Override
    public boolean check() {
        if (!graphExistFunction.apply(graphName)) {
            log.error("图谱不存在");
            return false;
        }
        return true;
    }
}
