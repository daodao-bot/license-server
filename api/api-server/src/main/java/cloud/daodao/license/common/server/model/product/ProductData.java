package cloud.daodao.license.common.server.model.product;

import cloud.daodao.license.common.constant.AppConstant;
import cloud.daodao.license.common.model.Serializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(title = "ProductData", description = "产品数据")
@Data
public class ProductData implements Serializer {

    @Schema(title = "ID", description = "ID", example = "1")
    @NotNull
    @Min(value = 1)
    @Max(value = Long.MAX_VALUE)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @Schema(title = "产品代码", description = "产品代码：支持最长 32 位字符串，可以包含数字、大小写字母、- 或 _", example = "demo")
    @NotEmpty
    @Size(min = 1, max = 32)
    @Pattern(regexp = "^[0-9A-Za-z-_]{1,32}$")
    private String code;

    @Schema(title = "产品名称", description = "产品名称", example = "演示")
    @NotEmpty
    @Size(min = 1, max = 64)
    private String name;

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
