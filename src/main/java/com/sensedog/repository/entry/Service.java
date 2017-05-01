package com.sensedog.repository.entry;

import com.sensedog.system.SystemStatus;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "service")
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    private Integer id;

    @OneToOne(mappedBy = "service")
    @Cascade(CascadeType.SAVE_UPDATE)
    private AlarmDevice alarmDevice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_user_id")
    private MasterUser masterUser;

    @Column(name = "master_user_auth_token")
    private String masterAuthToken;

    @Column
    @Enumerated(EnumType.STRING)
    private SystemStatus status;

    @Column(name = "service_name")
    private String serviceName;

    @OneToOne(mappedBy = "service")
    @Cascade(CascadeType.SAVE_UPDATE)
    private PinCode pinCode;

    @Column(name = "creation_date")
    private ZonedDateTime creationDate;

    @OneToMany(mappedBy = "service", fetch = FetchType.LAZY)
    private List<Detection> detections;

    @OneToMany(mappedBy = "service", fetch = FetchType.LAZY)
    private List<Subscriber> subscribers;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMasterAuthToken() {
        return masterAuthToken;
    }

    public void setMasterAuthToken(String masterAuthToken) {
        this.masterAuthToken = masterAuthToken;
    }

    public AlarmDevice getAlarmDevice() {
        return alarmDevice;
    }

    public void setAlarmDevice(AlarmDevice alarmDevice) {
        this.alarmDevice = alarmDevice;
    }

    public MasterUser getMasterUser() {
        return masterUser;
    }

    public void setMasterUser(MasterUser masterUser) {
        this.masterUser = masterUser;
    }

    public PinCode getPinCode() {
        return pinCode;
    }

    public void setPinCode(PinCode pinCode) {
        this.pinCode = pinCode;
    }

    public SystemStatus getStatus() {
        return status;
    }

    public void setStatus(SystemStatus status) {
        this.status = status;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public List<Detection> getDetections() {
        return detections;
    }

    public void setDetections(List<Detection> detections) {
        this.detections = detections;
    }

    public List<Subscriber> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(List<Subscriber> subscribers) {
        this.subscribers = subscribers;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
