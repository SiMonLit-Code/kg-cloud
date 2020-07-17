package ai.plantdata.kgcloud.sdk.req.app.function;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 实体提示功能
 *
 * @author cjw
 * @version 1.0
 * @date 2019/12/6 17:09
 */
public interface PromptSearchInterface {
    /**
     * 关键字
     *
     * @return 。
     */
    String getKw();

    /**
     * 概念id
     *
     * @return 。
     */
    List<Long> getConceptIds();

    /**
     * 概念是否继承
     *
     * @return 。
     */
    Boolean getInherit();

    /**
     * 分页偏移量
     *
     * @return 。
     */
    int getOffset();

    /**
     * 分页数量
     *
     * @return 。
     */
    int getLimit();
}
