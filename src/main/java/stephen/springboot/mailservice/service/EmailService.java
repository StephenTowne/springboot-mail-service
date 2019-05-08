package stephen.springboot.mailservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stephen.springboot.mailservice.dto.EmailInputDTO;
import stephen.springboot.mailservice.dto.EmailOutputDTO;
import stephen.springboot.mailservice.entity.Email;
import stephen.springboot.mailservice.repository.EmailRepository;

import java.sql.Timestamp;

@Service
@Slf4j
public class EmailService {
    /**
     * Email数据操作类
     */
    @Autowired
    private EmailRepository emailRepository;

    /**
     * AMQP操作类
     */
    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * spring-boot-starter-mail 会自动配置该Bean
     */
    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * 发送方用户名
     */
    @Value("${spring.mail.username}")
    private String senderUserName;


    @Transactional
    public EmailOutputDTO getEmailById(Long id) {
        EmailOutputDTO emailOutputDTO = new EmailOutputDTO();
        emailOutputDTO.doConvert(emailRepository.getOne(id));
        return emailOutputDTO;
    }

    @Transactional
    public void deleteEmailById(Long id) {
        emailRepository.deleteById(id);
    }

    @Transactional
    public Long sendEmail(EmailInputDTO emailInputDTO) {
        Email email = emailInputDTO.doReconvert();
        email.setId(null);
        email.setStatus(Email.STATUS_READY_TO_SEND);
        email.setCreateTime(new Timestamp(System.currentTimeMillis()));
        email = emailRepository.save(email);

        // 发送信息到RabbitMQ
        amqpTemplate.convertAndSend(email);

        return email.getId();
    }

    /**
     * 通过@RabbitHandler指定消息的处理方法
     *
     * @param email
     */
    @RabbitListener(queues = "mail_service")
    @RabbitHandler
    public void processMsg(Email email) {
        // 发送消息
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderUserName);
        String[] emailAddresses = email.getReceivers().split(",");
        message.setTo(emailAddresses);
        message.setSubject(email.getSubject());
        message.setText(email.getContent());
        javaMailSender.send(message);

        // 更新email记录
        email.setStatus(Email.STATUS_SEND_SUCCESS);
        email.setSendTime(new Timestamp(System.currentTimeMillis()));
        emailRepository.save(email);
    }
}
