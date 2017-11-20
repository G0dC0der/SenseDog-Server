package com.sensedog.system;

public class ServerSettings {

    private final String cloudKey;
    private final String twiloSender;
    private final String twiloAccountToken;
    private final String twiloAuthToken;
    private final String sendgridSender;
    private final String sendgridKey;
    private final Integer pincodeDuration;
    private final Integer gpsSafeRadius;

    private ServerSettings(final Builder builder) {
        cloudKey = builder.cloudKey;
        twiloSender = builder.twiloSender;
        twiloAccountToken = builder.twiloAccountToken;
        twiloAuthToken = builder.twiloAuthToken;
        sendgridSender = builder.sendgridSender;
        sendgridKey = builder.sendgridKey;
        gpsSafeRadius = builder.gpsSafeRadius;
        pincodeDuration = builder.pincodeDuration;
    }

    public String getCloudKey() {
        return cloudKey;
    }

    public String getTwiloSender() {
        return twiloSender;
    }

    public String getTwiloAccountToken() {
        return twiloAccountToken;
    }

    public String getTwiloAuthToken() {
        return twiloAuthToken;
    }

    public String getSendgridSender() {
        return sendgridSender;
    }

    public String getSendgridKey() {
        return sendgridKey;
    }

    public Integer getGpsSafeRadius() {
        return gpsSafeRadius;
    }

    public Integer getPincodeDuration() {
        return pincodeDuration;
    }

    static Builder builder() {
        return new Builder();
    }

    static class Builder {
        private String cloudKey;
        private String twiloSender;
        private String twiloAccountToken;
        private String twiloAuthToken;
        private String sendgridSender;
        private String sendgridKey;
        private Integer pincodeDuration;
        private Integer gpsSafeRadius;

        private Builder() {
        }

        Builder cloudKey(final String cloudKey) {
            this.cloudKey = cloudKey;
            return this;
        }

        Builder twiloSender(final String twiloSender) {
            this.twiloSender = twiloSender;
            return this;
        }

        Builder twiloAccountToken(final String twiloAccountToken) {
            this.twiloAccountToken = twiloAccountToken;
            return this;
        }

        Builder twiloAuthToken(final String twiloAuthToken) {
            this.twiloAuthToken = twiloAuthToken;
            return this;
        }

        Builder sendgridSender(final String sendgridSender) {
            this.sendgridSender = sendgridSender;
            return this;
        }

        Builder sendgridKey(final String sendgridKey) {
            this.sendgridKey = sendgridKey;
            return this;
        }

        Builder gpsSafeRadius(final Integer gpsSafeRadius) {
            this.gpsSafeRadius = gpsSafeRadius;
            return this;
        }

        Builder pincodeDuration(final Integer pincodeDuration) {
            this.pincodeDuration = pincodeDuration;
            return this;
        }

        ServerSettings build()  {
            return new ServerSettings(this);
        }
    }
}
