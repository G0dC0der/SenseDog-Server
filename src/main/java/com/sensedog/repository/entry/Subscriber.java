package com.sensedog.repository.entry;

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
public class Subscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscriber_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @Cascade(CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "service_id")
    private Service service;

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
    private List<SubscriberCapability> subscriberCapabilities;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

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

    public int getNotifyRegularity() {
        return notifyRegularity;
    }

    public void setNotifyRegularity(Integer notifyRegularity) {
        this.notifyRegularity = notifyRegularity;
    }

    public ZonedDateTime getLastNotificationDate() {
        return lastNotificationDate;
    }

    public void setLastNotificationDate(ZonedDateTime lastNotificationDate) {
        this.lastNotificationDate = lastNotificationDate;
    }

    public DetectionType getLastDetectionType() {
        return lastDetectionType;
    }

    public void setLastDetectionType(DetectionType lastDetectionType) {
        this.lastDetectionType = lastDetectionType;
    }

    public Severity getLastSeverity() {
        return lastSeverity;
    }

    public void setLastSeverity(Severity lastSeverity) {
        this.lastSeverity = lastSeverity;
    }

    public Severity getMinimumSeverity() {
        return minimumSeverity;
    }

    public void setMinimumSeverity(Severity minimumSeverity) {
        this.minimumSeverity = minimumSeverity;
    }

    public String getSecurityKey() {
        return securityKey;
    }

    public void setSecurityKey(String securityKey) {
        this.securityKey = securityKey;
    }

    public List<SubscriberCapability> getSubscriberCapabilities() {
        return subscriberCapabilities;
    }

    public void setSubscriberCapabilities(List<SubscriberCapability> subscriberCapabilities) {
        this.subscriberCapabilities = subscriberCapabilities;
    }
}
