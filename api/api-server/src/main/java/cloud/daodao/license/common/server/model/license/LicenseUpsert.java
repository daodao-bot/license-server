package cloud.daodao.license.common.server.model.license;

import cloud.daodao.license.common.annotation.Cipher;
import cloud.daodao.license.common.annotation.Hash;
import cloud.daodao.license.common.annotation.Mask;
import cloud.daodao.license.common.constant.AppConstant;
import cloud.daodao.license.common.model.Serializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Schema(title = "LicenseUpsert", description = "License 写入")
@Data
public class LicenseUpsert implements Serializer {

    @Schema(title = "ID", description = "ID", example = "1")
    @Min(value = 1)
    @Max(value = Long.MAX_VALUE)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @Schema(title = "产品 id", description = "产品 id", example = "1")
    @NotNull
    @Min(value = 1)
    @Max(value = Long.MAX_VALUE)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long productId;

    @Schema(title = "客户 id", description = "客户 id", example = "1")
    @NotNull
    @Min(value = 1)
    @Max(value = Long.MAX_VALUE)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long customerId;

    @Schema(title = "应用 id", description = "应用 id", example = "00000000000000000000000000000000")
    @Size(min = 1, max = 32)
    private String appId;

    @Schema(title = "License", description = "许可证（授权码）", example = "00000000000000000000000000000000")
    @Size(min = 32, max = 32)
    @Pattern(regexp = "^[0-9A-Fa-f]{32}$")
    private String license;

    @Schema(title = "License 密文", description = "License 密文", example = "", hidden = true)
    @Cipher(property = "license", security = Cipher.Security.AES)
    @Size(min = 1, max = 128)
    @Pattern(regexp = "^[0-9A-Fa-f]{1,128}$")
    private String licenseCipher;

    @Schema(title = "License HASH", description = "License HASH", example = "", hidden = true)
    @Hash(property = "license", security = Hash.Security.SHA_256)
    @Size(min = 64, max = 64)
    @Pattern(regexp = "^[0-9A-Fa-f]{64}$")
    private String licenseHash;

    @Schema(title = "License 掩码", description = "License 掩码", example = "", hidden = true)
    @Mask(property = "license", security = Mask.Security.LICENSE)
    @Size(min = 1, max = 32)
    private String licenseMask;

    @Schema(title = "有效期开始", description = "有效期开始", example = AppConstant.DATE_EXAMPLE)
    @NotNull
    @PastOrPresent
    private LocalDate periodStart;

    @Schema(title = "有效期结束", description = "有效期结束", example = AppConstant.DATE_EXAMPLE)
    @Future
    private LocalDate periodEnd;

    @Schema(title = "是否长期有效", description = "是否长期有效：FALSE 有限期，TRUE 长期", example = "false")
    @NotNull
    private Boolean longTerm;

    @Schema(title = "是否有效", description = "是否有效", example = "true")
    private Boolean valid;

}
