package cloud.daodao.license.server.helper;

import cloud.daodao.license.common.server.model.mail.MailSend;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * @author DaoDao
 */
@Slf4j
@Component
public class MailHelper {

    @Value("${spring.mail.username:}")
    private String from;

    @Resource
    private JavaMailSender javaMailSender;

    @Async
    public void sendMail(MailSend mailSend) {
        log.info("准备发送邮件: {}", mailSend);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        String[] to = mailSend.getTo();
        String[] bcc = mailSend.getBcc();
        String[] cc = mailSend.getCc();
        String replyTo = mailSend.getReplyTo();
        String subject = mailSend.getSubject();
        String text = mailSend.getText();
        Integer priority = mailSend.getPriority();
        LocalDateTime sentDate = mailSend.getSentDate();
        try {
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(to);
            if (null != bcc) {
                mimeMessageHelper.setBcc(bcc);
            }
            if (null != cc) {
                mimeMessageHelper.setCc(cc);
            }
            if (null != replyTo && !replyTo.isEmpty()) {
                mimeMessageHelper.setReplyTo(replyTo);
            }
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(text);
            if (null != priority) {
                mimeMessageHelper.setPriority(priority);
            }
            if (null != sentDate) {
                mimeMessageHelper.setSentDate(Date.from(sentDate.atZone(ZoneOffset.systemDefault()).toInstant()));
            }

            javaMailSender.send(mimeMessage);
            log.info("邮件发送成功!!!");
        } catch (MessagingException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

    }

}
