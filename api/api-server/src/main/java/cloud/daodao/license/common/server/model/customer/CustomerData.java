package cloud.daodao.license.common.server.model.customer;

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

import java.time.LocalDateTime;

@Schema(title = "CustomerData", description = "客户数据")
@Data
public class CustomerData implements Serializer {

    @Schema(title = "ID", description = "ID", example = "1")
    @NotNull
    @Min(value = 1)
    @Max(value = Long.MAX_VALUE)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @Schema(title = "名称", description = "名称", example = "嘟嘟")
    @NotEmpty
    @Size(min = 1, max = 64)
    private String name;

    @Schema(title = "名称密文", description = "名称密文", example = "00000000000000000000000000000000")
    @Cipher(property = "name", security = Cipher.Security.AES)
    @NotEmpty
    @Size(min = 1, max = 128)
    @Pattern(regexp = "^[0-9A-Fa-f]{1,128}$")
    private String nameCipher;

    @Schema(title = "名称 HASH", description = "名称 HASH", example = "00000000000000000000000000000000")
    @Hash(property = "name", security = Hash.Security.SHA_256)
    @NotEmpty
    @Size(min = 64, max = 64)
    @Pattern(regexp = "^[0-9A-Fa-f]{64}$")
    private String nameHash;

    @Schema(title = "名称掩码", description = "名称掩码", example = "00000000000000000000000000000000")
    @Mask(property = "name", security = Mask.Security.NAME)
    @NotEmpty
    @Size(min = 1, max = 32)
    private String nameMask;

    @Schema(title = "手机号", description = "手机号", example = "10000000000")
    @NotEmpty
    @Size(min = 11, max = 11)
    @Pattern(regexp = "^1[0-9]{10}$")
    private String phone;

    @Schema(title = "手机号密文", description = "手机号密文", example = "00000000000000000000000000000000")
    @Cipher(property = "phone", security = Cipher.Security.AES)
    @NotEmpty
    @Size(min = 1, max = 64)
    @Pattern(regexp = "^[0-9A-Fa-f]{1,64}$")
    private String phoneCipher;

    @Schema(title = "手机号 HASH", description = "手机号 HASH", example = "00000000000000000000000000000000")
    @Hash(property = "phone", security = Hash.Security.SHA_256)
    @NotEmpty
    @Size(min = 64, max = 64)
    @Pattern(regexp = "^[0-9A-Fa-f]{64}$")
    private String phoneHash;

    @Schema(title = "手机号掩码", description = "手机号掩码", example = "00000000000000000000000000000000")
    @Mask(property = "phone", security = Mask.Security.PHONE)
    @NotEmpty
    @Size(min = 1, max = 32)
    private String phoneMask;

    @Schema(title = "邮箱", description = "邮箱", example = "daodao-bot@daodao.cloud")
    @NotEmpty
    @Size(min = 1, max = 64)
    @Email
    private String email;

    @Schema(title = "邮箱密文", description = "邮箱密文", example = "00000000000000000000000000000000")
    @Cipher(property = "email", security = Cipher.Security.AES)
    @NotEmpty
    @Size(min = 1, max = 128)
    @Pattern(regexp = "^[0-9A-Fa-f]{1,128}$")
    private String emailCipher;

    @Schema(title = "邮箱 HASH", description = "邮箱 HASH", example = "00000000000000000000000000000000")
    @Hash(property = "email", security = Hash.Security.SHA_256)
    @NotEmpty
    @Size(min = 64, max = 64)
    @Pattern(regexp = "^[0-9A-Fa-f]{64}$")
    private String emailHash;

    @Schema(title = "邮箱掩码", description = "邮箱掩码", example = "00000000000000000000000000000000")
    @Mask(property = "email", security = Mask.Security.EMAIL)
    @NotEmpty
    @Size(min = 1, max = 32)
    private String emailMask;

    @Schema(title = "创建时间", description = "创建时间", example = AppConstant.DATE_TIME_EXAMPLE)
    @NotNull
    private LocalDateTime createTime;

    @Schema(title = "更新时间", description = "更新时间", example = AppConstant.DATE_TIME_EXAMPLE)
    @NotNull
    private LocalDateTime updateTime;

    @Schema(title = "是否有效", description = "是否有效", example = "true")
    @NotNull
    private Boolean valid;

}
