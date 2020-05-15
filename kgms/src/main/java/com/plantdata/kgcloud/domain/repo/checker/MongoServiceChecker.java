package com.plantdata.kgcloud.domain.repo.checker;

import com.plantdata.kgcloud.exception.BizException;

import java.util.function.Function;

/**
 * @author cjw
 * @date 2020/5/15  11:12
 */
public class MongoServiceChecker implements ServiceChecker {

    private Function<String, Boolean> graphExistFunction;
    private String graphName;

    public MongoServiceChecker(Function<String, Boolean> graphExistFunction, String graphName) {
        this.graphExistFunction = graphExistFunction;
        this.graphName = graphName;
    }

    @Override
    public void check() {
        if (!graphExistFunction.apply(graphName)) {
            throw new BizException("图谱不存在");
        }
    }
}
