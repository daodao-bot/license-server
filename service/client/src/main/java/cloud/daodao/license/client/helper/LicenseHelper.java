package cloud.daodao.license.client.helper;

import cloud.daodao.license.client.config.AppConfig;
import cloud.daodao.license.common.client.error.ClientError;
import cloud.daodao.license.common.constant.AppConstant;
import cloud.daodao.license.common.error.AppException;
import cloud.daodao.license.common.model.Request;
import cloud.daodao.license.common.model.Response;
import cloud.daodao.license.common.model.Serializer;
import cloud.daodao.license.common.server.api.LicenseApi;
import cloud.daodao.license.common.server.model.license.LicenseData;
import cloud.daodao.license.common.server.model.license.LicenseParam;
import cloud.daodao.license.common.util.security.AesUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.stream.IntStream;

@Slf4j
@Component
public class LicenseHelper {

    @Resource
    private AppConfig appConfig;

    @Resource
    private LicenseApi licenseApi;

    @Resource
    private RestTemplate restTemplate;

    public LicenseData licenseIntrospect(LicenseParam param) {
        if (appConfig.getLicenseApiSecurity()) {
            return licenseIntrospectSecurity(param);
        } else {
            return licenseIntrospectUnSecurity(param);
        }
    }

    /**
     * 非安全模式
     */
    public LicenseData licenseIntrospectUnSecurity(LicenseParam param) {
        String appId = appConfig.getLicenseAppId();
        Request<LicenseParam> request = new Request<>(param);
        Response<LicenseData> response;
        try {
            response = licenseApi.licenseIntrospect(appId, request);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new AppException(ClientError.INVOKE_SERVER_ERROR, e);
        }
        if (!response.isOk()) {
            throw new AppException(response.getCode(), response.getMessage());
        }
        return response.getData();
    }

    /**
     * 安全模式
     */
    public LicenseData licenseIntrospectSecurity(LicenseParam param) {
        String appId = appConfig.getLicenseAppId();
        String license = param.getLicense();
        String aesKey = IntStream.range(0, license.length()).filter(i -> i % 2 == 0).mapToObj(license::charAt).collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
        String aesIv = IntStream.range(0, license.length()).filter(i -> i % 2 == 1).mapToObj(license::charAt).collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
        String paramPlains = param.toJson();
        String paramCipher = null;
        try {
            paramCipher = AesUtil.encrypt(aesKey, aesIv, paramPlains);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new AppException(ClientError.AES_ENCRYPT_ERROR, e);
        }
        URI uri = URI.create(appConfig.getLicenseServer() + "/api/license/introspect");
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.set(AppConstant.X_SECURITY, AppConstant.AES);
        requestHeaders.set(AppConstant.X_APP_ID, appId);
        Request<String> request = new Request<>(paramCipher);
        RequestEntity<Request<String>> requestEntity = new RequestEntity<>(request, requestHeaders, HttpMethod.POST, uri);
        ResponseEntity<Response<String>> responseEntity;
        try {
            responseEntity = restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>() {
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new AppException(ClientError.INVOKE_SERVER_ERROR, e);
        }
        Response<String> response = responseEntity.getBody();
        assert response != null;
        if (!response.isOk()) {
            throw new AppException(response.getCode(), response.getMessage());
        }
        String dataCipher = response.getData();
        String dataPlains;
        try {
            dataPlains = AesUtil.decrypt(aesKey, aesIv, dataCipher);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new AppException(ClientError.AES_DECRYPT_ERROR, e);
        }
        LicenseData data = Serializer.ofJson(dataPlains, new TypeReference<>() {
        });
        return data;
    }

}
