package cloud.daodao.license.server.runner;

import cloud.daodao.license.common.model.PageData;
import cloud.daodao.license.common.model.PageParam;
import cloud.daodao.license.common.server.model.customer.CustomerData;
import cloud.daodao.license.common.server.model.customer.CustomerSearch;
import cloud.daodao.license.common.server.model.customer.CustomerUpsert;
import cloud.daodao.license.common.server.model.license.LicenseData;
import cloud.daodao.license.common.server.model.license.LicenseSearch;
import cloud.daodao.license.common.server.model.license.LicenseUpsert;
import cloud.daodao.license.server.config.AppConfig;
import cloud.daodao.license.server.constant.LicenseConstant;
import cloud.daodao.license.server.service.CustomerService;
import cloud.daodao.license.server.service.LicenseService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Order(4)
@Component
public class AdminRunner implements ApplicationRunner {

    @Resource
    private JdbcClient jdbcClient;

    @Resource
    private AppConfig appConfig;

    @Resource
    private CustomerService customerService;

    @Resource
    private LicenseService licenseService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("正在检查 admin 数据...");
        String adminUsername = appConfig.getAdminUsername();
        CustomerSearch customerSearch = new CustomerSearch();
        customerSearch.setName(adminUsername);
        PageData<CustomerData> customerDataPageData = customerService.customerSearch(new PageParam<>(customerSearch));
        CustomerData customerData;
        if (customerDataPageData.getList().isEmpty()) {
            log.info("准备写入 customer 数据!!!");
            CustomerUpsert customerUpsert = new CustomerUpsert();
            customerUpsert.setName(adminUsername);
            customerUpsert.setPhone("");
            customerUpsert.setEmail("");
            customerData = customerService.customerUpsert(customerUpsert);
            log.info("customer 数据写入完成!!!");
        } else {
            log.info("customer 数据已存在!!!");
            customerData = customerDataPageData.getList().getFirst();
        }

        Map<String, Object> params = new HashMap<>();
        params.put("code", LicenseConstant.LICENSE_ADMIN_PRODUCT_CODE);
        params.put("name", LicenseConstant.LICENSE_ADMIN_PRODUCT_CODE);

        String sql = """
                SELECT * FROM `product` WHERE `code` = :code AND `name` = :name;
                """;
        Map<String, Object> map = jdbcClient.sql(sql)
                .params(params)
                .query()
                .singleRow();
        Long productId = (Long.valueOf(String.valueOf(map.get("id"))));
        Long customerId = customerData.getId();
        LicenseSearch licenseSearch = new LicenseSearch();
        licenseSearch.setProductId(productId);
        licenseSearch.setCustomerId(customerId);
        LicenseData licenseData;
        PageData<LicenseData> licenseDataPageData = licenseService.licenseSearch(new PageParam<>(licenseSearch));
        if (licenseDataPageData.getList().isEmpty()) {
            log.info("准备写入 license 数据!!!");
            LicenseUpsert licenseUpsert = new LicenseUpsert();
            licenseUpsert.setProductId(productId);
            licenseUpsert.setCustomerId(customerId);
            licenseUpsert.setAppId(appConfig.getAdminAppId());
            licenseUpsert.setLicense(appConfig.getAdminLicense());
            licenseData = licenseService.licenseUpsert(licenseUpsert);
            log.info("license 数据写入完成!!!");
        } else {
            log.info("license 数据已存在!!!");
            licenseData = licenseDataPageData.getList().getFirst();
        }
        assert licenseData != null;
        log.info("admin 数据检查完成!!!");
    }

}
