package cloud.daodao.license.server.controller;

import cloud.daodao.license.common.model.Ok;
import cloud.daodao.license.common.model.Request;
import cloud.daodao.license.common.model.Response;
import cloud.daodao.license.common.server.api.MailApi;
import cloud.daodao.license.common.server.model.mail.MailSend;
import cloud.daodao.license.server.service.MailService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author DaoDao
 */
@RestController
public class MailController implements MailApi {

    @Resource
    private MailService mailService;

    @Override
    public Response<Ok> mailSend(@RequestBody @Valid Request<MailSend> request) {
        mailService.mailSend(request.getParam());
        return Response.ok();
    }

}
