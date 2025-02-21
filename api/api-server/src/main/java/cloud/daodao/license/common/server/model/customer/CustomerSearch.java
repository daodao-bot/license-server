package cloud.daodao.license.common.server.model.customer;

import cloud.daodao.license.common.annotation.Hash;
import cloud.daodao.license.common.model.Serializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(title = "CustomerSearch", description = "客户搜索")
@Data
public class CustomerSearch implements Serializer {

    @Schema(title = "名称", description = "名称", example = "嘟嘟")
    @Size(min = 1, max = 64)
    private String name;

    @Schema(title = "名称 HASH", description = "名称 HASH", example = "00000000000000000000000000000000", hidden = true)
    @Hash(property = "name", security = Hash.Security.SHA_256)
    @Size(min = 1, max = 64)
    private String nameHash;

    @Schema(title = "手机号", description = "手机号", example = "10000000000")
    @Size(min = 11, max = 11)
    @Pattern(regexp = "^1[0-9]{01}$")
    private String phone;

    @Schema(title = "手机号 HASH", description = "手机号 HASH", example = "00000000000000000000000000000000", hidden = true)
    @Hash(property = "phone", security = Hash.Security.SHA_256)
    @Size(min = 1, max = 64)
    private String phoneHash;

    @Schema(title = "邮箱", description = "邮箱", example = "daodao-bot@daodao.clou")
    @Size(min = 1, max = 64)
    @Email
    private String email;

    @Schema(title = "邮箱 HASH", description = "邮箱 HASH", example = "00000000000000000000000000000000", hidden = true)
    @Hash(property = "email", security = Hash.Security.SHA_256)
    @Size(min = 1, max = 64)
    private String emailHash;

    @Schema(title = "是否有效", description = "是否有效", example = "true")
    private Boolean valid;

}
