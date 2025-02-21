package cloud.daodao.license.server.controller;

import cloud.daodao.license.common.model.PageData;
import cloud.daodao.license.common.model.PageParam;
import cloud.daodao.license.common.model.Request;
import cloud.daodao.license.common.model.Response;
import cloud.daodao.license.common.server.api.CustomerApi;
import cloud.daodao.license.common.server.model.IdParam;
import cloud.daodao.license.common.server.model.customer.CustomerData;
import cloud.daodao.license.common.server.model.customer.CustomerSearch;
import cloud.daodao.license.common.server.model.customer.CustomerUpsert;
import cloud.daodao.license.server.service.CustomerService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author DaoDao
 */
@RestController
public class CustomerController implements CustomerApi {

    @Resource
    private CustomerService customerService;

    @Override
    public Response<PageData<CustomerData>> customerSearch(Request<PageParam<CustomerSearch>> request) {
        PageData<CustomerData> data = customerService.customerSearch(request.getParam());
        return new Response<>(data);
    }

    @Override
    public Response<CustomerData> customerSelect(Request<IdParam> request) {
        CustomerData data = customerService.customerSelect(request.getParam());
        return new Response<>(data);
    }

    @Override
    public Response<CustomerData> customerUpsert(Request<CustomerUpsert> request) {
        CustomerData data = customerService.customerUpsert(request.getParam());
        return new Response<>(data);
    }

}
