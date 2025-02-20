package cloud.daodao.license.client.controller;

import cloud.daodao.license.client.service.TestService;
import cloud.daodao.license.common.client.api.TestApi;
import cloud.daodao.license.common.client.model.TestParam;
import cloud.daodao.license.common.model.Ok;
import cloud.daodao.license.common.model.Request;
import cloud.daodao.license.common.model.Response;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author DaoDao
 */
@RestController
public class TestController implements TestApi {

    @Resource
    private TestService testService;

    @Override
    public Response<Ok> testLicense(Request<TestParam> request) {
        testService.testLicense(request.getParam());
        return Response.ok();
    }

}
