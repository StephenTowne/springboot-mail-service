## 项目描述
基于Springboot开发的邮件服务，提供Restful接口供外部调用
可用于学习Springboot

## 数据流转
Restful API -> 存储Mysql & 发送消息至RabbitMQ -> 消费消息并发送邮件

## 依赖项
1. RabbitMQ
2. Mysql

## 启动
1. 执行根目录的oa_email.sql
2. 修改数据库配置
3. 修改RabbitMQ配置
4. 修改邮箱配置
5. mvn spring-boot:run
