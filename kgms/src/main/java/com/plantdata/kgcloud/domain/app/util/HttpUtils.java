package com.plantdata.kgcloud.domain.app.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author DingHao
 *
 */
public final class HttpUtils {

	private static final RestTemplate restTemplate;

	static{
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		factory.setReadTimeout(60000);
		factory.setConnectTimeout(60000);
		restTemplate = new RestTemplate(factory);
//		restTemplate.getInterceptors().add(new HttpInterceptor());
	}

	public static <T> T getForObject(String url, MultiValueMap<String, Object> query, Class<T> responseType) {
		return restTemplate.getForObject(parseQueryParams(url,query),responseType);
	}

	public static <T> T postForObject(String url, MultiValueMap<String, Object> query, MultiValueMap<String, Object> post, Class<T> responseType) {
		return restTemplate.postForObject(parseQueryParams(url,query),post,responseType);
	}

	public static <T> T postForObject(String url, MultiValueMap<String, Object> query, MultiValueMap<String, Object> post, ParameterizedTypeReference<T> responseType) {
		return restTemplate.exchange(parseQueryParams(url,query), HttpMethod.POST,new HttpEntity<>(post),responseType).getBody();
	}

	private static String parseQueryParams(String url,MultiValueMap<String,Object> query){
		if(query != null && query.size() > 0){
			StringBuilder sb = new StringBuilder(url).append("?");
			for (Map.Entry<String, List<Object>> item : query.entrySet()) {
				sb.append(item.getKey()).append("=").append(item.getValue().size()>0?item.getValue().get(0).toString():null);
			}
			return sb.toString();
		}
		return url;
	}

	static class HttpInterceptor implements ClientHttpRequestInterceptor {

		private Logger logger = LoggerFactory.getLogger(HttpInterceptor.class);

		@Override
		public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
			logger.info("请求地址：{}", request.getURI());
			logger.info("请求方法： {}", request.getMethod());
			logger.info("请求内容：{}", new String(body));
			logger.info("请求头：{}", request.getHeaders());
			return execution.execute(request, body);
		}
	}

}
