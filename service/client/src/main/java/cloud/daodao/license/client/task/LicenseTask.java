package cloud.daodao.license.client.task;

import cloud.daodao.license.client.config.AppConfig;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class LicenseTask {

    @Resource
    private AppConfig appConfig;

    @Scheduled(cron = "0 0 0 * * ?")
    public void periodValidator() {
        log.info("License 有效期校验任务开始...");

    }

}
