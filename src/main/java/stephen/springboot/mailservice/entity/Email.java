package stephen.springboot.mailservice.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "oa_email")
@Getter
@Setter
@Accessors(chain = true)
public class Email implements Serializable {
    public static final long serialVersionUID = 1L;

    public final static String STATUS_READY_TO_SEND = "READY_TO_SEND";
    public final static String STATUS_SEND_SUCCESS = "SEND_SUCCESS";
    public final static String STATUS_SEND_FAIL = "SEND_FAIL";

    /**
     * 自增ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    /**
     * 收件人列表，以逗号,分隔
     */
    @Column(name = "receivers", nullable = false)
    private String receivers;

    /**
     * 邮件标题
     */
    @Column(name = "subject", nullable = false, length = 200)
    private String subject;

    /**
     * 邮件内容
     */
    @Column(name = "content", nullable = false)
    private String content;

    /**
     * 邮件状态
     */
    @Column(name = "status")
    private String status;

    /**
     * 发送时间
     */
    @Column(name = "send_time")
    private Timestamp sendTime;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Timestamp createTime = new Timestamp(System.currentTimeMillis());
}
