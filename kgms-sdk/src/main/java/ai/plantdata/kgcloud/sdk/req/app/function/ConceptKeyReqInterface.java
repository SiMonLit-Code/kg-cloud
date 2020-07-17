package ai.plantdata.kgcloud.sdk.req.app.function;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/20 18:26
 */
public interface ConceptKeyReqInterface {

    default Long getConceptId() {
        return null;
    }

    default void setConceptId(Long conceptId) {
    }

    default String getConceptKey() {
        return null;
    }

    default void setConceptKey(String conceptKey) {
    }

}
