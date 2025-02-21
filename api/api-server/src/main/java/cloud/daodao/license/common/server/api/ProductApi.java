package cloud.daodao.license.common.server.api;

import cloud.daodao.license.common.constant.AppConstant;
import cloud.daodao.license.common.model.PageData;
import cloud.daodao.license.common.model.PageParam;
import cloud.daodao.license.common.model.Request;
import cloud.daodao.license.common.model.Response;
import cloud.daodao.license.common.server.constant.ServerConstant;
import cloud.daodao.license.common.server.model.IdParam;
import cloud.daodao.license.common.server.model.product.ProductData;
import cloud.daodao.license.common.server.model.product.ProductSearch;
import cloud.daodao.license.common.server.model.product.ProductUpsert;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * @author DaoDao
 */
@Tag(name = "ProductApi", description = "产品接口")
@HttpExchange(url = AppConstant.API)
public interface ProductApi {

    @Operation(summary = "产品搜索", description = "@DaoDao 产品搜索")
    @PostExchange(url = ServerConstant.PRODUCT_SEARCH)
    Response<PageData<ProductData>> productSearch(@RequestBody @Valid Request<PageParam<ProductSearch>> request);

    @Operation(summary = "产品查询", description = "@DaoDao 产品查询")
    @PostExchange(url = ServerConstant.PRODUCT_SELECT)
    Response<ProductData> productSelect(@RequestBody @Valid Request<IdParam> request);

    @Operation(summary = "产品写入", description = "@DaoDao 产品写入")
    @PostExchange(url = ServerConstant.PRODUCT_UPSERT)
    Response<ProductData> productUpsert(@RequestBody @Valid Request<ProductUpsert> request);

}
