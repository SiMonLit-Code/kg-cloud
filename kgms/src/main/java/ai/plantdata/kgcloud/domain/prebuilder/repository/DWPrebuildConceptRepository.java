package ai.plantdata.kgcloud.domain.prebuilder.repository;

import ai.plantdata.kgcloud.domain.prebuilder.entity.DWPrebuildConcept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DWPrebuildConceptRepository extends JpaRepository<DWPrebuildConcept, Integer> {

    @Query(value = "select * from prebuild_concept where model_id = :modelId and `id` in (:conceptIds)",nativeQuery = true)
    List<DWPrebuildConcept> findByModelAndConceptIds(@Param("modelId") Integer modelId, @Param("conceptIds") List<Integer> conceptIds);
}
