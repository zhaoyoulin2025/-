package com.ruoyi.system.domain;

import java.util.Date;

public class MessageDTO {
    private final String messageType;
    private final String receiverId;
    private final String title;
    private final String content;
    private final int status;
    private final Date sendTime;
    private final Date readTime;

    private MessageDTO(Builder builder) {
        this.messageType = builder.messageType;
        this.receiverId = builder.receiverId;
        this.title = builder.title;
        this.content = builder.content;
        this.status = builder.status;
        this.sendTime = builder.sendTime;
        this.readTime = builder.readTime;
    }



    public String getMessageType() {
        return messageType;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getStatus() {
        return status;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public Date getReadTime() {
        return readTime;
    }

    // Builder 静态内部类
    public static class Builder {
        private final String receiverId;// 必选参数
        private String messageType;     // 可选参数
        private String title;           // 可选参数
        private String content;         // 可选参数
        private int status;// 可选参数（默认值）
        private Date sendTime;          // 可选参数
        private Date readTime;          // 可选参数

        // 必选参数通过构造函数传入
        public Builder( String receiverId) {
            this.receiverId = receiverId;
        }

        // 可选参数通过方法链设置
        public Builder messageType(String messageType) {
            this.messageType = messageType;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public Builder sendTime(Date sendTime) {
            this.sendTime = sendTime;
            return this;
        }

        public Builder readTime(Date readTime) {
            this.readTime = readTime;
            return this;
        }

        // 构建方法，包含参数校验
        public MessageDTO build() {
            // 验证必选参数
            if (receiverId == null || receiverId.isEmpty()) {
                throw new IllegalArgumentException("接收者 ID 不能为空");
            }

            // 验证可选但有业务规则的参数
            if (sendTime == null) {
                this.sendTime = new Date(); // 默认使用当前时间
            }

            return new MessageDTO(this);
        }
    }

    @Override
    public String toString() {
        return "MessageDTO{" +
                ", messageType='" + messageType + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", status='" + status + '\'' +
                ", sendTime=" + sendTime +
                ", readTime=" + readTime +
                '}';
    }
}
