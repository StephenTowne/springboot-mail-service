package stephen.springboot.mailservice.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import stephen.springboot.mailservice.common.Converter;
import stephen.springboot.mailservice.entity.Email;
import stephen.springboot.mailservice.validator.IsEmail;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Accessors(chain = true)
public class EmailInputDTO implements Converter<Email> {
    @ApiModelProperty("邮件ID，非必须，新建时不要传")
    private Long id;

    /**
     * 收件人列表，以逗号,分隔
     */
    @IsEmail
    @ApiModelProperty("收件人地址，逗号,分隔")
    private String receivers;

    /**
     * 邮件标题
     */
    @NotEmpty(message = "邮件标题不能为空")
    @ApiModelProperty("邮件标题")
    private String subject;

    /**
     * 邮件内容
     */
    @NotEmpty(message = "邮件不能为空")
    @ApiModelProperty("邮件内容")
    private String content;

    @Override
    public void doConvert(Email email) {
        this.content = email.getContent();
        this.id = email.getId();
        this.receivers = email.getReceivers();
        this.subject = email.getSubject();
    }

    @Override
    public Email doReconvert() {
        return new Email()
                .setId(getId())
                .setReceivers(getReceivers())
                .setSubject(getSubject())
                .setContent(getContent());
    }
}
