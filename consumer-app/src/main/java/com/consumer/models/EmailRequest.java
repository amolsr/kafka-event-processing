package com.consumer.models;

import lombok.Data;
import java.util.List;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

@Data
public class EmailRequest {
    private String templateBody;
    private String subject;
    private String eventCode;
    private List<String> tags;
    private Map<String, Object> extraFields;
    private Map<String, Object> commonData;
    private String commId;
    private List<RecipientGroup> to;
    private Sender commonSender;
    private CommonFrom commonFrom;
    private int delay;
    private int messageDelay;
    private List<Object> commonAttachments;

    public List<Object> getCommonAttachments() {
        if (this.commonAttachments == null) {
            this.commonAttachments = Arrays.asList();
        }
        return this.commonAttachments;
    }

    public Map<String, Object> getExtraFields() {
        if (this.extraFields == null) {
            this.extraFields = new HashMap<>();
        }
        return this.extraFields;
    }

    public Map<String, Object> getCommonData() {
        if (this.commonData == null) {
            this.commonData = new HashMap<>();
        }
        return this.commonData;
    }

    public String getCommId() {
        if (this.commId == null) {
            this.commId = "";
        }
        return this.commId;
    }

    @Data
    public static class RecipientGroup {
        private String medium;
        private List<Recipient> recipients;
        private List<Object> cc;
        private List<Object> bcc;
        private List<Object> replyTo;
        private Sender from;
        private Sender sender;
        private Map<String, Object> buildData;
        private List<Object> attachments;

        public List<Object> getCc() {
            if (this.cc == null) {
                this.cc = Arrays.asList();
            }
            return this.cc;
        }

        public List<Object> getBcc() {
            if (this.bcc == null) {
                this.bcc = Arrays.asList();
            }
            return this.bcc;
        }

        public List<Object> getReplyTo() {
            if (this.replyTo == null) {
                this.replyTo = Arrays.asList();
            }
            return this.replyTo;
        }

        public List<Object> getAttachments() {
            if (this.attachments == null) {
                this.attachments = Arrays.asList();
            }
            return this.attachments;
        }
    }

    @Data
    public static class Recipient {
        private String id;
        private String name;
        private String userId;
    }

    @Data
    public static class Sender {
        private String id;
        private String name;
    }

    @Data
    public static class CommonFrom {
        private Sender mail;
    }
}
