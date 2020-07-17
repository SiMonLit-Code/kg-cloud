package ai.plantdata.kgcloud.domain.share.repository;

import ai.plantdata.kgcloud.domain.share.entity.LinkShare;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
public interface LinkShareRepository extends JpaRepository<LinkShare, Long> {
    /**
     * 查询分享连接
     *
     * @param userId
     * @param kgName
     * @return
     */
    List<LinkShare> findByUserIdAndKgName(String userId, String kgName);

    /**
     * 查询分享连接
     *
     * @param kgName
     * @param spaId
     * @return
     */
    Optional<LinkShare> findByKgNameAndSpaId(String kgName, String spaId);

}
