package com.sensedog.rest.model;

import com.sensedog.repository.model.SqlSubscriber;

public class ApiSubscriberView {

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

    public static ApiSubscriberView from(final SqlSubscriber subscriber) {
        final ApiSubscriberView subscriberInfo = new ApiSubscriberView();
        subscriberInfo.setEmail(subscriber.getEmail());
        subscriberInfo.setName(subscriber.getName());
        subscriberInfo.setPhone(subscriber.getPhone());

        return subscriberInfo;
    }
}
