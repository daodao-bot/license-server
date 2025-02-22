package cloud.daodao.license.server.runner;

import cloud.daodao.license.server.constant.LicenseConstant;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Order(3)
@Component
public class TableRunner implements ApplicationRunner {

    @Resource
    private JdbcClient jdbcClient;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("正在检查初始数据...");

        Map<String, Object> params = new HashMap<>();
        params.put("code", LicenseConstant.LICENSE_ADMIN_PRODUCT_CODE);
        params.put("name", LicenseConstant.LICENSE_ADMIN_PRODUCT_CODE);

        String sql = """
                SELECT * FROM `product` WHERE `code` = :code AND `name` = :name;
                """;
        List<Map<String, Object>> list = jdbcClient.sql(sql)
                .params(params)
                .query()
                .listOfRows();
        if (list.isEmpty()) {
            log.info("准备写入 product !!!");
            sql = """
                INSERT INTO `product` (`code`, `name`)
                VALUES (:code, :name);
                """;
            int update = jdbcClient.sql(sql)
                    .params(params)
                    .update();
            log.info("执行结果: {}", update);
        }
        log.info("初始数据检查完成!!!");
    }

}
