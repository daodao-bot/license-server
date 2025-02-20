package cloud.daodao.license.common.health;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class StartupHealthIndicator implements HealthIndicator {

    @Autowired(required = false)
    private StartupHealth startupHealth;

    private Status status = Status.UNKNOWN;

    private Map<String, Object> details = new HashMap<>();

    @Override
    public Health health() {
        Health health;
        if (Status.UP.equals(status)) {
            health = new Health.Builder(status, details).build();
        } else if (null != startupHealth) {
            health = startupHealth.health();
            status = health.getStatus();
            details = health.getDetails();
            log.info("startup health : {}", health);
        } else {
            status = Status.UP;
            details.put("startup", "silence");
            health = new Health.Builder(status, details).build();
        }
        return health;
    }

}
