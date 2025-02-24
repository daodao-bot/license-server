package cloud.daodao.license.common.server.model.admin;

import cloud.daodao.license.common.model.Serializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(title = "ConfigData", description = "配置数据")
@Data
public class ConfigData implements Serializer {

    @Schema(title = "应用 id", description = "应用 id", example = "00000000000000000000000000000000")
    @Size(min = 32, max = 32)
    @Pattern(regexp = "^[0-9A-Fa-f]{32}$")
    private String appId;

    @Schema(title = "License", description = "许可证（授权码）", example = "00000000000000000000000000000000")
    @Size(min = 32, max = 32)
    @Pattern(regexp = "^[0-9A-Fa-f]{32}$")
    private String license;

    @Schema(title = "API 安全", description = "是否开启 API 强安全校验", example = "true")
    @NotNull
    private Boolean apiSecurity;

}
