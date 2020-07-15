package ai.plantdata.kgcloud.domain.repo.service;


import java.util.List;

/**
 * @author cjw
 * @date 2020/5/22  9:41
 */
public interface RepositoryMenuService {

    void saveOrUpdate(List<Integer> menuIdList, Integer repositoryId);
}
