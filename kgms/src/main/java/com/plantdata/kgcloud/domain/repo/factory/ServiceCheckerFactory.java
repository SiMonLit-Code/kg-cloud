package com.plantdata.kgcloud.domain.repo.factory;

import com.plantdata.kgcloud.domain.repo.checker.ConsulServiceChecker;
import com.plantdata.kgcloud.domain.repo.checker.FileServiceChecker;
import com.plantdata.kgcloud.domain.repo.checker.MongoServiceChecker;
import com.plantdata.kgcloud.domain.repo.checker.ServiceChecker;
import com.plantdata.kgcloud.domain.repo.model.ConsulService;
import com.plantdata.kgcloud.domain.repo.model.FileService;
import com.plantdata.kgcloud.domain.repo.model.MongoService;
import com.plantdata.kgcloud.domain.repo.model.RepositoryRoot;
import com.plantdata.kgcloud.exception.BizException;

/**
 * @author cjw
 * @date 2020/5/15  11:55
 */
public class ServiceCheckerFactory {


    private RepositoryRoot repositoryRoot;

    public ServiceCheckerFactory(RepositoryRoot repositoryRoot) {
        this.repositoryRoot = repositoryRoot;
    }

    public ServiceChecker factory() {
        switch (repositoryRoot.checkType()) {
            case CONSUL:
                return consulChecker();
            case FILE:
                return fileChecker();
            case MONGO:
                return mongoChecker();
            default:
                throw new BizException("组件类型错误");
        }
    }

    private ConsulServiceChecker consulChecker() {
        assert repositoryRoot instanceof ConsulService;
        ConsulService manager = (ConsulService) this.repositoryRoot;
        return new ConsulServiceChecker(manager.discoveryClient(), manager.handlers());
    }

    private FileServiceChecker fileChecker() {
        assert repositoryRoot instanceof FileService;
        return new FileServiceChecker(((FileService) repositoryRoot).filePaths());
    }

    private MongoServiceChecker mongoChecker() {
        assert repositoryRoot instanceof MongoService;
        //todo
        return new MongoServiceChecker(null, null);
    }
}
