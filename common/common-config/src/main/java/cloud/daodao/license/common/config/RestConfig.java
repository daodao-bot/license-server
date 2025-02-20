package cloud.daodao.license.common.config;

import cloud.daodao.license.common.interceptor.RestLoggerInterceptor;
import jakarta.annotation.Resource;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfig {

    @Resource
    private RestLoggerInterceptor restLoggerInterceptor;

    @Bean
    public RestClient.Builder builder(RestTemplateBuilder restTemplateBuilder) {
        // restTemplateBuilder.interceptors(restLoggerInterceptor);
        restTemplateBuilder.additionalInterceptors(restLoggerInterceptor);
        BufferingClientHttpRequestFactory requestFactory = new BufferingClientHttpRequestFactory(restTemplateBuilder.buildRequestFactory());
        RestClient.Builder builder = RestClient.builder(restTemplateBuilder.build());
        builder.requestInterceptors(interceptors -> {
            if (!interceptors.contains(restLoggerInterceptor)) {
                interceptors.add(restLoggerInterceptor);
            }
        });
        builder.requestFactory(requestFactory);
        return builder;
    }

    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        // builder.requestInterceptor(restLoggerInterceptor);
        builder.requestInterceptors(interceptors -> {
            if (!interceptors.contains(restLoggerInterceptor)) {
                interceptors.add(restLoggerInterceptor);
            }
        });
        return builder.build();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // builder.interceptors(restLoggerInterceptor);
        builder.additionalInterceptors(restLoggerInterceptor);
        RestTemplate restTemplate = builder.build();
        if (!restTemplate.getInterceptors().contains(restLoggerInterceptor)) {
            restTemplate.getInterceptors().add(restLoggerInterceptor);
        }
        BufferingClientHttpRequestFactory requestFactory = new BufferingClientHttpRequestFactory(builder.buildRequestFactory());
        restTemplate.setRequestFactory(requestFactory);
        return restTemplate;
    }

}
