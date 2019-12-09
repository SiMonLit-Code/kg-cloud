package com.plantdata.kgcloud.domain.share.repository;

import com.plantdata.kgcloud.domain.share.entity.LinkShare;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
public interface LinkShareRepository extends JpaRepository<LinkShare, Long> {
    List<LinkShare> findByUserIdAndKgName(String userId, String kgName);

    Optional<LinkShare> findByKgNameAndSpaId(String kgName, String spaId);

}
