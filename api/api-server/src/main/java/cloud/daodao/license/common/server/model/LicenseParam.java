package cloud.daodao.license.common.server.model;

import cloud.daodao.license.common.model.Serializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(title = "LicenseParam", description = "license 参数")
@Data
public class LicenseParam implements Serializer {

    @Schema(title = "license", description = "许可证（授权码）", example = "00000000000000000000000000000000")
    @NotEmpty
    @Size(min = 32, max = 32)
    @Pattern(regexp = "^[0-9a-fA-F]{32}$")
    private String license;

}
