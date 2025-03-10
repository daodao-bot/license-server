package cloud.daodao.license.common.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public interface StartupHealth {

    default Health health() {
        Status status = Status.UP;
        Map<String, Object> details = new HashMap<>();
        details.put("startup", "success");
        return new Health.Builder(status, details).build();
    }

}
