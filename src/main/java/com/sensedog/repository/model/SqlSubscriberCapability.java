package com.sensedog.repository.model;

import com.sensedog.security.Capability;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "subscriber_capability")
public class SqlSubscriberCapability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscriber_capability_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscriber_id")
    private SqlSubscriber subscriber;

    @Column
    @Enumerated(EnumType.STRING)
    private Capability capability;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public SqlSubscriber getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(final SqlSubscriber subscriber) {
        this.subscriber = subscriber;
    }

    public Capability getCapability() {
        return capability;
    }

    public void setCapability(final Capability capability) {
        this.capability = capability;
    }
}
