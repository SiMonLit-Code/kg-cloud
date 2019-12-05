package com.plantdata.kgcloud.domain.model.repository;

import com.plantdata.kgcloud.domain.model.entity.Model;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
public interface ModelRepository extends JpaRepository<Model, Long> {
    /**
     * 根据用户和id删除模型
     *
     * @param id
     * @param userId
     */
    void deleteByIdAndUserId(Long id, String userId);

    /**
     * 根据用户和id查询模型
     * @param id
     * @param userId
     * @return
     */
    Optional<Model> findByIdAndUserId(Long id, String userId);
}
