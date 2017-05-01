package com.sensedog.service.domain;

import com.sensedog.repository.entry.Subscriber;

public class SubscriberInfo {

    private String name;
    private String phone;
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static SubscriberInfo from(Subscriber subscriber) {
        SubscriberInfo subscriberInfo = new SubscriberInfo();
        subscriberInfo.setEmail(subscriber.getEmail());
        subscriberInfo.setName(subscriber.getName());
        subscriberInfo.setPhone(subscriber.getPhone());

        return subscriberInfo;
    }
}
