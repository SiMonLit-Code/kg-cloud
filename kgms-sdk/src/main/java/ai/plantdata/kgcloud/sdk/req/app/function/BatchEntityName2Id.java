package ai.plantdata.kgcloud.sdk.req.app.function;

import java.util.List;

/**
 * @author cjw
 * @date 2020/8/12  10:43
 */
public interface BatchEntityName2Id {
    /**
     * 实体ids
     *
     * @return
     */
    List<Long> getIds();

    /**
     * 实体名称
     *
     * @return
     */
    List<String> getKws();

    /**
     * 设置实体ids
     *
     * @param ids
     */
    void setIds(List<Long> ids);
}
