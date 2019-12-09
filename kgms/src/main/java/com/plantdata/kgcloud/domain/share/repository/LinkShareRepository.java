package com.plantdata.kgcloud.domain.share.repository;

import com.plantdata.kgcloud.domain.share.entity.LinkShare;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
public interface LinkShareRepository extends JpaRepository<LinkShare, Long> {
    List<LinkShare> findByUserId( String userId,String kgName);

    Boolean cancelShare( String kgName, String spaId);

    Boolean shareLink(String kgName,String spaId);

    LinkShare findByKgNameAndSpaId( String kgName,  String spaId);

    Integer hasShareRole( String userId);

    int updateShareLink(Integer id,String link);

    LinkShare findByUserId(LinkShare probe);

    Boolean save(String userId, String kgName, String spaId, Integer status);
}
