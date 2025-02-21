package cloud.daodao.license.common.server.model.license;

import cloud.daodao.license.common.annotation.Hash;
import cloud.daodao.license.common.model.Serializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(title = "LicenseParam", description = "License 参数")
@Data
public class LicenseParam implements Serializer {

    @Schema(title = "license", description = "许可证（授权码）", example = "00000000000000000000000000000000")
    @NotEmpty
    @Size(min = 32, max = 32)
    @Pattern(regexp = "^[0-9A-Fa-f]{32}$")
    private String license;

    @Schema(title = "License HASH", description = "License HASH", example = "", hidden = true)
    @Hash(property = "license", security = Hash.Security.SHA_256)
    @Size(min = 64, max = 64)
    @Pattern(regexp = "^[0-9A-Fa-f]{64}$")
    private String licenseHash;

}
