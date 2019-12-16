package com.plantdata.kgcloud.domain.app.util;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.*;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/6 16:20
 */
@Slf4j
public class EsUtils {

    public static boolean isChinese(String words) {
        Pattern chinesePattern = compile("[\\u4E00-\\u9FA5]+");
        Matcher matcherResult = chinesePattern.matcher(words);
        return matcherResult.find();
    }

    private static final String POST = "post";

    private static HttpHost buildHttpHost(String addr) {
        String[] address = addr.split(":");
        if (address.length == 2) {
            String ip = address[0];
            int port = Integer.parseInt(address[1]);
            return new HttpHost(ip, port);
        }
        return null;
    }

    public static EsQuery buildEsQuery(List<String> addressList) {
        HttpHost[] hosts = addressList.stream()
                .map(EsUtils::buildHttpHost)
                .filter(Objects::nonNull)
                .toArray(HttpHost[]::new);
        RestClient build = RestClient.builder(hosts).setMaxRetryTimeoutMillis(60000).build();
        return new EsQuery(hosts, build);
    }

    @AllArgsConstructor
    private static class EsQuery {
        private HttpHost[] hosts;
        private RestClient client;

        private void close() {
            if (Objects.nonNull(client)) {
                try {
                    client.close();
                } catch (IOException e) {
                    log.error("es资源关闭错误:" + e.getMessage());
                }
            }
        }

        private void consume(HttpEntity entity) {
            if (entity == null) {
                return;
            }
            if (entity.isStreaming()) {
                final InputStream instream;
                try {
                    instream = entity.getContent();
                    if (instream != null) {
                        instream.close();
                    }
                } catch (IOException e) {
                    log.error("es资源关闭错误:" + e.getMessage());
                }
            }
        }
    }


    public static String sendPost(EsQuery esQuery, List<String> indexes, List<String> types, String query) {
        String rs = null;
        String index = StringUtils.join(indexes, ",");
        String type = "/";
        if (!CollectionUtils.isEmpty(types)) {
            type += StringUtils.join(types, ",") + "/";
        }
        HttpEntity entity = new NStringEntity(query, ContentType.APPLICATION_JSON);
        String endpoint = "/" + index + type + "_search";
        Request request = new Request(POST, endpoint);
        request.setEntity(entity);
        request.addParameter("pretty", "true");
        try {
            Response response = esQuery.client.performRequest(request);
            rs = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            log.error("es错误:{}", e.getMessage());
        } finally {
            esQuery.consume(entity);
            esQuery.close();
        }
        return rs;
    }
}
