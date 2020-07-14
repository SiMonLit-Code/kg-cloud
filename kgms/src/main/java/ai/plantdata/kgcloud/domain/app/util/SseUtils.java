package ai.plantdata.kgcloud.domain.app.util;

import ai.plantdata.cloud.exception.BizException;
import ai.plantdata.cloud.util.JacksonUtils;
import ai.plantdata.kgcloud.constant.AppErrorCodeEnum;
import cn.hiboot.mcn.core.model.result.RestResp;
import com.fasterxml.jackson.databind.JavaType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ResolvableType;
import org.springframework.util.MultiValueMap;

/**
 * use rest template
 *
 * @author: DingHao
 * @date: 2019/6/27 11:07
 */
@Slf4j
public class SseUtils {

    public static <T> T postForObject(String url, MultiValueMap<String, Object> query, MultiValueMap<String, Object> post, Class<T> responseType) {
        return sendRequest(url, query, post, responseType, false);
    }

    private static <T> T sendRequest(String url, MultiValueMap<String, Object> query, MultiValueMap<String, Object> post, Class<T> responseType, boolean isGet) {
        RestResp<T> restResp;
        String rs;
        if (isGet) {
            rs = HttpUtils.getForObject(url, query, String.class);
        } else {
            rs = HttpUtils.postForObject(url, query, post, String.class);
        }
        try {
            JavaType javaType = JacksonUtils.getInstance().constructType(ResolvableType.forClassWithGenerics(RestResp.class, responseType).getType());
            restResp = JacksonUtils.readValue(rs, javaType);
        } catch (Exception e) {
            log.error("参数解析错误;访问url：{};query参数:{};post参数:{},返回结果:{}", url, JsonUtils.objToJson(query), JsonUtils.objToJson(post), rs);
            throw BizException.of(AppErrorCodeEnum.PARAM_ANALYSIS_ERROR);
        }
        if (restResp.getActionStatus().equals(RestResp.ActionStatusMethod.FAIL)) {
            log.error("远程500错误;访问url：{};query参数:{};post参数:{},返回结果:{}", url, JsonUtils.objToJson(query), JsonUtils.objToJson(post), rs);
            throw new BizException(restResp.getErrorInfo());
        }
        return restResp.getData();
    }


}
