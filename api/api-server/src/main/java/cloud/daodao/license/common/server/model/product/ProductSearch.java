package cloud.daodao.license.common.server.model.product;

import cloud.daodao.license.common.model.Serializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(title = "ProductSearch", description = "产品搜索")
@Data
public class ProductSearch implements Serializer {

    @Schema(title = "产品代码", description = "产品代码：支持最长 32 位字符串，可以包含数字、大小写字母、- 或 _", example = "demo")
    @Size(min = 1, max = 32)
    @Pattern(regexp = "^[0-9A-Za-z-_]{1,32}$")
    private String code;

    @Schema(title = "产品名称", description = "产品名称", example = "演示")
    @Size(min = 1, max = 64)
    private String name;

    @Schema(title = "valid", description = "是否有效", example = "true")
    private Boolean valid;

}
