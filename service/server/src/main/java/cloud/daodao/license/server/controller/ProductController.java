package cloud.daodao.license.server.controller;

import cloud.daodao.license.common.model.PageData;
import cloud.daodao.license.common.model.PageParam;
import cloud.daodao.license.common.model.Request;
import cloud.daodao.license.common.model.Response;
import cloud.daodao.license.common.server.api.ProductApi;
import cloud.daodao.license.common.server.model.IdParam;
import cloud.daodao.license.common.server.model.product.ProductData;
import cloud.daodao.license.common.server.model.product.ProductSearch;
import cloud.daodao.license.common.server.model.product.ProductUpsert;
import cloud.daodao.license.server.service.ProductService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author DaoDao
 */
@RestController
public class ProductController implements ProductApi {

    @Resource
    private ProductService productService;

    @Override
    public Response<PageData<ProductData>> productSearch(Request<PageParam<ProductSearch>> request) {
        PageData<ProductData> data = productService.productSearch(request.getParam());
        return new Response<>(data);
    }

    @Override
    public Response<ProductData> productSelect(Request<IdParam> request) {
        ProductData data = productService.productSelect(request.getParam());
        return new Response<>(data);
    }

    @Override
    public Response<ProductData> productUpsert(Request<ProductUpsert> request) {
        ProductData data = productService.productUpsert(request.getParam());
        return new Response<>(data);
    }

}
