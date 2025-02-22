package cloud.daodao.license.common.server.model.user;

import cloud.daodao.license.common.model.Serializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(title = "UserData", description = "用户数据")
@Data
public class UserData implements Serializer {

    @Schema(title = "用户名称", description = "用户名称", example = "admin")
    @NotEmpty
    @Size(min = 1, max = 64)
    @Pattern(regexp = "^[a-zA-Z0-9-_.]{1,64}$")
    private String username;

}
