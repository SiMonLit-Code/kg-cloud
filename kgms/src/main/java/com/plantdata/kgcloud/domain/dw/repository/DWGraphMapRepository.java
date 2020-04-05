package com.plantdata.kgcloud.domain.dw.repository;

import com.plantdata.kgcloud.domain.dw.entity.DWGraphMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DWGraphMapRepository extends JpaRepository<DWGraphMap, Integer> {

    @Query(value = "select data_base_id from dw_graph_map where kg_name = :kgName group by data_base_id",nativeQuery = true)
    List<Long> getDatabaseList(@Param("kgName") String kgName);
}
