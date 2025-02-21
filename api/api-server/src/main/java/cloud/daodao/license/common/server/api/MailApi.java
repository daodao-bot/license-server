package cloud.daodao.license.common.server.api;

import cloud.daodao.license.common.constant.AppConstant;
import cloud.daodao.license.common.model.Ok;
import cloud.daodao.license.common.model.Request;
import cloud.daodao.license.common.model.Response;
import cloud.daodao.license.common.server.constant.ServerConstant;
import cloud.daodao.license.common.server.model.mail.MailSend;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;

@Tag(name = "MailApi", description = "邮件接口")
@HttpExchange(url = AppConstant.API)
public interface MailApi {

    @Operation(summary = "发送邮件", description = "@DaoDao 发送邮件")
    @PostMapping(value = ServerConstant.MAIL_SEND)
    Response<Ok> mailSend(@RequestBody @Valid Request<MailSend> request);

}
