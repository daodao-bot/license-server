package cloud.daodao.license.server;

import cloud.daodao.license.common.constant.AppConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author DaoDao
 */
@EnableScheduling
@SpringBootApplication(scanBasePackages = {AppConstant.BASE_PACKAGE})
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

}
