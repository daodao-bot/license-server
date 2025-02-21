package cloud.daodao.license.server.controller;

import cloud.daodao.license.common.model.Request;
import cloud.daodao.license.common.model.Response;
import cloud.daodao.license.common.server.constant.ServerConstant;
import cloud.daodao.license.common.server.model.license.LicenseData;
import cloud.daodao.license.common.server.model.license.LicenseParam;
import cloud.daodao.license.server.ServerApplicationTest;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class LicenseControllerTest extends ServerApplicationTest {

    @Test
    void licenseIntrospect() {
        String api = ServerConstant.LICENSE_INTROSPECT;

        LicenseParam param = new LicenseParam();
        param.setLicense("00000000000000000000000000000000");
        Request<?> request = new Request<>(param);

        Response<LicenseData> response = mockMvc(api, request, new TypeReference<>() {
        });

        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.isOk());

        LicenseData data = response.getData();

        Assertions.assertNotNull(data);
        Assertions.assertNotNull(data.getLicense());
    }

}