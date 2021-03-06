package ai.plantdata.kgcloud.config;

import ai.plantdata.kgcloud.domain.edit.util.ThreadLocalUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component("logFeignRequestInterceptor")
public class LogFeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        String batchNo = ThreadLocalUtils.getBatchNo();
        if (StringUtils.hasText(batchNo)) {
            requestTemplate.header("actionId", batchNo);
        }
    }
}
