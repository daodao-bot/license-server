package cloud.daodao.license.common.server.model.mail;

import cloud.daodao.license.common.constant.AppConstant;
import cloud.daodao.license.common.model.Serializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(title = "MailSend", description = "邮件发送")
@Data
public class MailSend implements Serializer {

    /**
     * 收件人
     */
    @Schema(title = "to", description = "收件人", example = "[\"daodao-bot@daodao.cloud\"]")
    @NotNull
    @Size(min = 1, max = 128)
    private String[] to;

    /**
     * 秘密抄送
     */
    @Schema(title = "bcc", description = "秘密抄送", example = "[\"daodao-bot@daodao.cloud\"]")
    @Size(min = 1, max = 128)
    private String[] bcc;

    /**
     * 抄送
     */
    @Schema(title = "cc", description = "抄送", example = "[\"daodao-bot@daodao.cloud\"]")
    @Size(min = 1, max = 128)
    private String[] cc;

    /**
     * 回复给
     */
    @Schema(title = "replyTo", description = "回复给", example = "回复：你好！")
    @Size(min = 1, max = 255)
    private String replyTo;

    /**
     * 主题
     */
    @Schema(title = "subject", description = "主题", example = "你好！")
    @NotNull
    @NotEmpty
    @Size(min = 1, max = 255)
    private String subject;

    /**
     * 内容
     */
    @Schema(title = "text", description = "内容", example = "你好！")
    @NotNull
    @NotEmpty
    @Size(min = 1, max = 1024000)
    private String text;

    /**
     * 优先级
     */
    @Schema(title = "priority", description = "优先级", example = "你好！")
    @Min(value = 1)
    @Max(value = 5)
    private Integer priority;

    /**
     * 发送时间
     */
    @Schema(title = "sentDate", description = "发送时间", example = AppConstant.DATE_TIME_EXAMPLE)
    @FutureOrPresent
    private LocalDateTime sentDate;

}
