package cloud.daodao.license.server.service;

import cloud.daodao.license.common.error.AppException;
import cloud.daodao.license.common.server.error.ServerError;
import cloud.daodao.license.common.server.model.mail.MailSend;
import cloud.daodao.license.server.helper.MailHelper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * @author DaoDao
 */
@Slf4j
@Service
public class MailService {

    @Resource
    private MailHelper mailHelper;

    public void mailSend(MailSend mailSend) {
        String[] to = mailSend.getTo();
        Arrays.stream(to).spliterator().forEachRemaining(this::check);
        String[] bcc = mailSend.getBcc();
        if (null != bcc) {
            Arrays.stream(bcc).spliterator().forEachRemaining(this::check);
        }
        String[] cc = mailSend.getCc();
        if (null != cc) {
            Arrays.stream(cc).spliterator().forEachRemaining(this::check);
        }
        mailHelper.sendMail(mailSend);
    }

    private void check(String mail) {
        if (null == mail || mail.isEmpty()) {
            throw new AppException(ServerError.MAIL_REGULAR_ERROR, mail);
        }
        String regex = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        boolean matches = Pattern.compile(regex).matcher(mail).matches();
        if (!matches) {
            throw new AppException(ServerError.MAIL_REGULAR_ERROR, mail);
        }
    }

}
