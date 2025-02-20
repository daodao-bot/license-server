package cloud.daodao.license.client;

import cloud.daodao.license.common.constant.AppConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author DaoDao
 */
@SpringBootApplication(scanBasePackages = {AppConstant.BASE_PACKAGE})
public class ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }

}
