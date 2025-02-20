package cloud.daodao.license.common.server.config;

import cloud.daodao.license.common.server.api.LicenseApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ServerClient {

    private final HttpServiceProxyFactory httpServiceProxyFactory;

    @Autowired
    public ServerClient(@Value("${app.service.license.server:server.license}") String service, RestClient.Builder builder) {
        String url = (service.matches("^https?://.*") ? service : "http://" + service) + "/";
        this.httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(builder.baseUrl(url).build())).build();
    }

    @Bean
    public LicenseApi licenseApi() {
        return httpServiceProxyFactory.createClient(LicenseApi.class);
    }

}
