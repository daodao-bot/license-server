package cloud.daodao.license.common.client.config;

import cloud.daodao.license.common.client.api.TestApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ClientClient {

    private final HttpServiceProxyFactory httpServiceProxyFactory;

    @Autowired
    public ClientClient(@Value("${app.service.license.client:client.license}") String service, RestClient.Builder builder) {
        String url = (service.matches("^https?://.*") ? service : "http://" + service) + "/";
        this.httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(builder.baseUrl(url).build())).build();
    }

    @Bean
    public TestApi testApi() {
        return httpServiceProxyFactory.createClient(TestApi.class);
    }

}
