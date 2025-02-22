package cloud.daodao.license.server.task;

import cloud.daodao.license.server.config.AppConfig;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class LicenseTask {

    @Resource
    private AppConfig appConfig;

    @Resource
    private JdbcClient jdbcClient;

    @Scheduled(cron = "0 0 0 * * ?")
    public void periodValidator() {
        log.info("License 有效期校验任务开始...");
        Long[] licenseExpiresWarningDays = appConfig.getLicenseExpiresWarningDays();
        Arrays.asList(licenseExpiresWarningDays).forEach(this::validator);
    }

    private void validator(Long days) {
        LocalDate now = LocalDate.now();
        String endDate = now.minusDays(days).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Map<String, Object> params = new HashMap<>();
        params.put("end_date", endDate);
        String sql = """
                SELECT
                    `customer`.`name_cipher` AS `name_cipher`,
                    `customer`.`email_cipher` AS `email_cipher`,
                    `product`.`name` AS `product_name`,
                    `license`.`license_mask` AS `license_mask`,
                    `license`.`period_end` AS `period_end`,
                    `license`.`app_id` AS `app_id`
                FROM `license`
                JOIN `product` ON `license`.`product_id` = `product`.`id`
                JOIN `customer` ON `license`.`customer_id` = `customer`.`id`
                WHERE `license`.`valid` = TRUE
                    AND `product`.`valid` = TRUE
                    AND `customer`.`valid` = TRUE
                    AND `license`.`long_term` != TRUE
                    AND `license`.`period_end` IS NOT NULL
                    AND `license`.`period_end` <= :end_date
                """;
        List<Map<String, Object>> list = jdbcClient.sql(sql)
                .params(params)
                .query()
                .listOfRows();
        list.forEach(map -> {
            String nameCipher = (String) map.get("name_cipher");
            String emailCipher = (String) map.get("email_cipher");
            String productName = (String) map.get("product_name");
            String licenseMask = (String) map.get("license_mask");
            String periodEnd = (String) map.get("period_end");
            String appId = (String) map.get("app_id");
            log.info("License 有效期校验任务，nameCipher: {}, emailCipher: {}, productName: {}, licenseMask: {}, periodEnd: {}, appId: {}",
                    nameCipher, emailCipher, productName, licenseMask, periodEnd, appId);
        });

        Boolean licenseExpiresWarningEmail = appConfig.getLicenseExpiresWarningEmail();
        if (licenseExpiresWarningEmail) {
            // 发送邮件
        }
    }

}
