package cloud.daodao.license.server.runner;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Order(2)
@Component
public class SchemaRunner implements ApplicationRunner {

    @Resource
    private JdbcClient jdbcClient;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("正在检查数据库表...");
        List<String> tables = new ArrayList<>();
        tables.add("product");
        tables.add("customer");
        tables.add("license");
        List<Object> showTables = jdbcClient.sql("SHOW TABLES")
                .query()
                .singleColumn();
        for (String table : tables) {
            if (!showTables.contains(table)) {
                createTable(table);
            }
        }
        log.info("数据库表检查完成!!!");
    }

    private void createTable(String table) throws IOException {
        log.info("准备创建数据库表: {}", table);
        String path = "schema/" + table + ".sql";
        ClassPathResource resource = new ClassPathResource(path);
        String sql = new String(resource.getInputStream().readAllBytes());
        log.info("执行 SQL: {}", sql);
        int update = jdbcClient.sql(sql).update();
        log.info("执行结果: {}", update);
    }

}
