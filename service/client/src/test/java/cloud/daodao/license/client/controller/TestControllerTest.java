package cloud.daodao.license.client.controller;

import cloud.daodao.license.client.ClientApplicationTest;
import cloud.daodao.license.common.client.constant.ClientConstant;
import cloud.daodao.license.common.client.model.TestParam;
import cloud.daodao.license.common.model.Ok;
import cloud.daodao.license.common.model.Request;
import cloud.daodao.license.common.model.Response;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class TestControllerTest extends ClientApplicationTest {

    @Test
    void testLicense() {
        String api = ClientConstant.TEST_LICENSE;

        TestParam param = new TestParam();
        param.setLicense("00000000000000000000000000000000");
        Request<?> request = new Request<>(param);

        Response<Ok> response = mockMvc(api, request, new TypeReference<>() {
        });

        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.isOk());

        Ok data = response.getData();

        Assertions.assertNotNull(data);
    }

}