package cloud.daodao.license.common.server.model.product;

import cloud.daodao.license.common.model.Serializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Schema(title = "ProductUpsert", description = "产品写入")
@Data
public class ProductUpsert implements Serializer {

    @Schema(title = "ID", description = "ID", example = "1")
    @Min(value = 1)
    @Max(value = Long.MAX_VALUE)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @Schema(title = "产品代码", description = "产品代码：支持最长 32 位字符串，可以包含数字、大小写字母、- 或 _", example = "demo")
    @NotEmpty
    @Size(min = 1, max = 32)
    @Pattern(regexp = "^[0-9A-Za-z-_]{1,32}$")
    private String code;

    @Schema(title = "产品名称", description = "产品名称：如果不传值，默认使用 code 的值", example = "演示")
    @Size(min = 1, max = 64)
    private String name;

    @Schema(title = "valid", description = "是否有效", example = "true")
    private Boolean valid;

}
