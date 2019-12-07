package com.plantdata.kgcloud.config;

import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/6 16:05
 */
@Configuration
@Slf4j
public class EsConfig {

    public static String cloudEsIp;
    public static Integer cloudEsRestPort;

    @Value("${cloud-es-ip}")
    public void setCloudEsIp(String cloudEsIp) {
        EsConfig.cloudEsIp = cloudEsIp;
    }

    @Value("${cloud-es-rest-port}")
    public void setCloudEsRestPort(Integer cloudEsRestPort) {
        EsConfig.cloudEsRestPort = cloudEsRestPort;
    }

    public static String getAddress() {
        List<String> strings = buildHost(cloudEsIp, cloudEsRestPort);
        Optional<String> reduce = strings.stream().reduce((a, b) -> a + "," + b);
        if (!reduce.isPresent()) {
            throw BizException.of(KgmsErrorCodeEnum.ES_CONFIG_NOT_FOUND);
        }
        return reduce.get();
    }

    private static List<String> buildHost(String ip, Integer port) {
        String[] address = ip.split(",");
        return Arrays.stream(address).map(addr -> addr + ":" + port).collect(Collectors.toList());

    }
}
