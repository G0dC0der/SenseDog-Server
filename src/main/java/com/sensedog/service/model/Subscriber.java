package com.sensedog.service.model;

import com.sensedog.repository.model.SqlSubscriber;

public class Subscriber {

    private String name;
    private String phone;
    private String email;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public static Subscriber from(final SqlSubscriber subscriber) {
        final Subscriber subscriberInfo = new Subscriber();
        subscriberInfo.setEmail(subscriber.getEmail());
        subscriberInfo.setName(subscriber.getName());
        subscriberInfo.setPhone(subscriber.getPhone());

        return subscriberInfo;
    }
}
