package cloud.daodao.license.common.client.api;

import cloud.daodao.license.common.client.constant.ClientConstant;
import cloud.daodao.license.common.client.model.TestParam;
import cloud.daodao.license.common.constant.AppConstant;
import cloud.daodao.license.common.model.Ok;
import cloud.daodao.license.common.model.Request;
import cloud.daodao.license.common.model.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * @author DaoDao
 */
@Tag(name = "TestApi", description = "测试接口")
@HttpExchange(url = AppConstant.API)
public interface TestApi {

    @Operation(summary = "测试 license", description = "测试 license @DaoDao")
    @PostExchange(url = ClientConstant.TEST_LICENSE)
    Response<Ok> testLicense(@RequestBody @Valid Request<TestParam> request);

}
