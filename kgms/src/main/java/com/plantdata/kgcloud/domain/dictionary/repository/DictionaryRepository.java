package com.plantdata.kgcloud.domain.dictionary.repository;

import com.plantdata.kgcloud.domain.dictionary.entity.Dictionary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
public interface DictionaryRepository extends JpaRepository<Dictionary, Long> {
    /**
     * 根据用户和id删除词典
     *
     * @param id
     * @param userId
     */
    void deleteByIdAndUserId(Long id, String userId);

    /**
     * 根据用户和id查询词典
     *
     * @param id
     * @param userId
     * @return
     */
    Optional<Dictionary> findByIdAndUserId(Long id, String userId);
}
