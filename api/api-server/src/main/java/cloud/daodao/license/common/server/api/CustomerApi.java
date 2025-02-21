package cloud.daodao.license.common.server.api;

import cloud.daodao.license.common.constant.AppConstant;
import cloud.daodao.license.common.model.PageData;
import cloud.daodao.license.common.model.PageParam;
import cloud.daodao.license.common.model.Request;
import cloud.daodao.license.common.model.Response;
import cloud.daodao.license.common.server.constant.ServerConstant;
import cloud.daodao.license.common.server.model.IdParam;
import cloud.daodao.license.common.server.model.customer.CustomerData;
import cloud.daodao.license.common.server.model.customer.CustomerSearch;
import cloud.daodao.license.common.server.model.customer.CustomerUpsert;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * @author DaoDao
 */
@Tag(name = "CustomerApi", description = "客户接口")
@HttpExchange(url = AppConstant.API)
public interface CustomerApi {

    @Operation(summary = "客户搜索", description = "@DaoDao 客户搜索")
    @PostExchange(url = ServerConstant.CUSTOMER_SEARCH)
    Response<PageData<CustomerData>> customerSearch(@RequestBody @Valid Request<PageParam<CustomerSearch>> request);

    @Operation(summary = "客户查询", description = "@DaoDao 客户查询")
    @PostExchange(url = ServerConstant.CUSTOMER_SELECT)
    Response<CustomerData> customerSelect(@RequestBody @Valid Request<IdParam> request);

    @Operation(summary = "客户写入", description = "@DaoDao 客户写入")
    @PostExchange(url = ServerConstant.CUSTOMER_UPSERT)
    Response<CustomerData> customerUpsert(@RequestBody @Valid Request<CustomerUpsert> request);

}
