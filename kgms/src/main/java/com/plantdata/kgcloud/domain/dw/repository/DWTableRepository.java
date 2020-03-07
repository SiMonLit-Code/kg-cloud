package com.plantdata.kgcloud.domain.dw.repository;

import com.plantdata.kgcloud.domain.dw.entity.DWTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DWTableRepository extends JpaRepository<DWTable, Long> {

    @Modifying
    @Query(value = "update dw_table set tbName = :tbName where tableName = :tableName and dw_database_id  = :databaseId", nativeQuery = true)
    void updateByTableName(@Param("tableName") String tableName, @Param("tbName")String tbName,@Param("databaseId")Long databaseId);

    List<DWTable> findByIdIn(List<Long> tableIds);
}
