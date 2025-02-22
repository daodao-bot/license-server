package cloud.daodao.license.common.server.model.user;

import cloud.daodao.license.common.model.Serializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(title = "LoginParam", description = "登录参数")
@Data
public class LoginParam implements Serializer {

    @Schema(title = "用户名称", description = "用户名称", example = "admin")
    @NotEmpty
    @Size(min = 1, max = 64)
    @Pattern(regexp = "^[a-zA-Z0-9-_.]{1,64}$")
    private String username;

    @Schema(title = "用户密码", description = "用户密码", example = "admin")
    @NotEmpty
    @Size(min = 1, max = 64)
    @Pattern(regexp = "^[a-zA-Z0-9-_.]{1,64}$")
    private String password;

}
