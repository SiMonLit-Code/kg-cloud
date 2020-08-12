package ai.plantdata.kgcloud.sdk.req.app.function;

/**
 * @author cjw
 * @date 2020/8/12  10:26
 */
public interface EntityName2Id {
    /**
     * 实体id
     * @return
     */
    Long getEntityId();

    /**
     * kw
     * @return
     */
    String getKw();

    /**
     * 设置实体id
     * @param entityId
     */
    void setEntityId(Long entityId);
}
