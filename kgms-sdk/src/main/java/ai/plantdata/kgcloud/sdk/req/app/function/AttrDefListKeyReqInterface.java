package ai.plantdata.kgcloud.sdk.req.app.function;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/17 17:23
 */
public interface AttrDefListKeyReqInterface {
    /**
     * 获取属性定义id
     *
     * @return 属性定义id
     */
    List<Integer> getAllowAttrs();

    /**
     * 设置属性定义id
     *
     * @param attrIds 属性定义id
     */
    void setAllowAttrs(List<Integer> attrIds);

    /**
     * 获取属性定义key
     *
     * @return 属性定义key
     */
    List<String> getAllowAttrsKey();
}
