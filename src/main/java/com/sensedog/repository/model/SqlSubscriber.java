package com.sensedog.repository.model;

import com.sensedog.detection.DetectionType;
import com.sensedog.detection.Severity;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "subscriber")
public class SqlSubscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscriber_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @Cascade(CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "service_id")
    private SqlService service;

    @Column
    private String name;

    @Column
    private String phone;

    @Column
    private String email;

    @Column(name = "last_notification_date")
    private ZonedDateTime lastNotificationDate;

    @Column(name = "last_notification_type")
    @Enumerated(EnumType.STRING)
    private DetectionType lastDetectionType;

    @Column(name = "last_notification_severity")
    @Enumerated(EnumType.STRING)
    private Severity lastSeverity;

    @Column(name = "notify_regularity")
    private Integer notifyRegularity;

    @Column(name = "minimum_severity")
    @Enumerated(EnumType.STRING)
    private Severity minimumSeverity;

    @Column(name = "security_key")
    private String securityKey;

    @OneToMany(mappedBy = "subscriber", fetch = FetchType.EAGER)
    @Cascade(CascadeType.SAVE_UPDATE)
    private List<SqlSubscriberCapability> subscriberCapabilities;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public SqlService getService() {
        return service;
    }

    public void setService(final SqlService service) {
        this.service = service;
    }

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

    public int getNotifyRegularity() {
        return notifyRegularity;
    }

    public void setNotifyRegularity(final Integer notifyRegularity) {
        this.notifyRegularity = notifyRegularity;
    }

    public ZonedDateTime getLastNotificationDate() {
        return lastNotificationDate;
    }

    public void setLastNotificationDate(final ZonedDateTime lastNotificationDate) {
        this.lastNotificationDate = lastNotificationDate;
    }

    public DetectionType getLastDetectionType() {
        return lastDetectionType;
    }

    public void setLastDetectionType(final DetectionType lastDetectionType) {
        this.lastDetectionType = lastDetectionType;
    }

    public Severity getLastSeverity() {
        return lastSeverity;
    }

    public void setLastSeverity(final Severity lastSeverity) {
        this.lastSeverity = lastSeverity;
    }

    public Severity getMinimumSeverity() {
        return minimumSeverity;
    }

    public void setMinimumSeverity(final Severity minimumSeverity) {
        this.minimumSeverity = minimumSeverity;
    }

    public String getSecurityKey() {
        return securityKey;
    }

    public void setSecurityKey(final String securityKey) {
        this.securityKey = securityKey;
    }

    public List<SqlSubscriberCapability> getSubscriberCapabilities() {
        return subscriberCapabilities;
    }

    public void setSubscriberCapabilities(final List<SqlSubscriberCapability> subscriberCapabilities) {
        this.subscriberCapabilities = subscriberCapabilities;
    }
}
