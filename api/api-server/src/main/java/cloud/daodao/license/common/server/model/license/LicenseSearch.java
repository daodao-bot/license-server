package cloud.daodao.license.common.server.model.license;

import cloud.daodao.license.common.annotation.Hash;
import cloud.daodao.license.common.constant.AppConstant;
import cloud.daodao.license.common.model.Serializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Schema(title = "LicenseSearch", description = "License 搜索")
@Data
public class LicenseSearch implements Serializer {

    @Schema(title = "产品 id", description = "产品 id", example = "1")
    @Min(value = 1)
    @Max(value = Long.MAX_VALUE)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long productId;

    @Schema(title = "客户 id", description = "客户 id", example = "1")
    @Min(value = 1)
    @Max(value = Long.MAX_VALUE)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long customerId;

    @Schema(title = "应用 id", description = "应用 id", example = "")
    @Size(min = 1, max = 32)
    private String appId;

    @Schema(title = "License", description = "许可证（授权码）", example = "00000000000000000000000000000000")
    @Size(min = 32, max = 32)
    @Pattern(regexp = "^[0-9A-Fa-f]{32}$")
    private String license;

    @Schema(title = "License HASH", description = "License HASH", example = "", hidden = true)
    @Hash(property = "license", security = Hash.Security.SHA_256)
    @Size(min = 64, max = 64)
    @Pattern(regexp = "^[0-9A-Fa-f]{64}$")
    private String licenseHash;

    @Schema(title = "有效期开始 - 下界", description = "有效期开始 - 下界", example = AppConstant.DATE_EXAMPLE)
    private LocalDate periodStartLower;

    @Schema(title = "有效期开始 - 上界", description = "有效期开始 - 上界", example = AppConstant.DATE_EXAMPLE)
    private LocalDate periodStartUpper;

    @Schema(title = "有效期结束 - 下界", description = "有效期结束 - 下界", example = AppConstant.DATE_EXAMPLE)
    private LocalDate periodEndLower;

    @Schema(title = "有效期结束 - 上界", description = "有效期结束 - 上界", example = AppConstant.DATE_EXAMPLE)
    private LocalDate periodEndUpper;

    @Schema(title = "是否长期有效", description = "是否长期有效：FALSE 有限期，TRUE 长期", example = "false")
    private Boolean longTerm;

    @Schema(title = "是否有效", description = "是否有效", example = "true")
    private Boolean valid;

}
