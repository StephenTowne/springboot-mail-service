package stephen.springboot.mailservice.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import stephen.springboot.mailservice.common.Converter;
import stephen.springboot.mailservice.entity.Email;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@Accessors(chain = true)
public class EmailOutputDTO implements Converter<Email> {
    @ApiModelProperty("邮件ID")
    private Long id;

    /**
     * 收件人列表，以逗号,分隔
     */
    @ApiModelProperty("收件人地址，逗号,分隔")
    private String receivers;

    /**
     * 邮件标题
     */
    @ApiModelProperty("邮件标题")
    private String subject;

    /**
     * 邮件内容
     */
    @ApiModelProperty("邮件内容")
    private String content;

    /**
     * 发送时间戳，DTO里面时间用字符串标示
     */
    @ApiModelProperty("发送时间")
    private String sendTime;

    /**
     * 创建时间戳
     */
    @ApiModelProperty("创建时间")
    private String createTime;

    @Override
    public void doConvert(Email email) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        this.content = email.getContent();
        this.id = email.getId();
        this.receivers = email.getReceivers();
        this.subject = email.getSubject();
        this.sendTime = email.getSendTime() == null ?
                "0000-00-00 00:00:00" : format.format(new Date(email.getSendTime().getTime()));
        this.createTime = email.getCreateTime() == null ?
                "0000-00-00 00:00:00" : format.format(new Date(email.getCreateTime().getTime()));
    }

    @Override
    public Email doReconvert() {
        return new Email()
                .setId(getId())
                .setReceivers(getReceivers())
                .setSubject(getSubject())
                .setContent(getContent())
                .setSendTime(Timestamp.valueOf(getSendTime()))
                .setCreateTime(Timestamp.valueOf(getCreateTime()));
    }
}
