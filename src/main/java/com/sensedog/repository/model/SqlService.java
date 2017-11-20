package com.sensedog.repository.model;

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
public class SqlService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    private Integer id;

    @OneToOne(mappedBy = "service")
    @Cascade(CascadeType.SAVE_UPDATE)
    private SqlAlarmDevice sqlAlarmDevice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_user_id")
    private SqlMaster masterUser;

    @Column(name = "master_user_auth_token")
    private String masterAuthToken;

    @Column
    @Enumerated(EnumType.STRING)
    private SystemStatus status;

    @Column(name = "service_name")
    private String serviceName;

    @OneToOne(mappedBy = "service")
    @Cascade(CascadeType.SAVE_UPDATE)
    private SqlPincode pinCode;

    @Column(name = "creation_date")
    private ZonedDateTime creationDate;

    @OneToMany(mappedBy = "service", fetch = FetchType.LAZY)
    private List<SqlDetection> detections;

    @OneToMany(mappedBy = "service", fetch = FetchType.LAZY)
    private List<SqlSubscriber> subscribers;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getMasterAuthToken() {
        return masterAuthToken;
    }

    public void setMasterAuthToken(final String masterAuthToken) {
        this.masterAuthToken = masterAuthToken;
    }

    public SqlAlarmDevice getAlarmDevice() {
        return sqlAlarmDevice;
    }

    public void setAlarmDevice(final SqlAlarmDevice sqlAlarmDevice) {
        this.sqlAlarmDevice = sqlAlarmDevice;
    }

    public SqlMaster getMasterUser() {
        return masterUser;
    }

    public void setMasterUser(final SqlMaster masterUser) {
        this.masterUser = masterUser;
    }

    public SqlPincode getPinCode() {
        return pinCode;
    }

    public void setPinCode(final SqlPincode pinCode) {
        this.pinCode = pinCode;
    }

    public SystemStatus getStatus() {
        return status;
    }

    public void setStatus(final SystemStatus status) {
        this.status = status;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(final ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public List<SqlDetection> getDetections() {
        return detections;
    }

    public void setDetections(final List<SqlDetection> detections) {
        this.detections = detections;
    }

    public List<SqlSubscriber> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(final List<SqlSubscriber> subscribers) {
        this.subscribers = subscribers;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(final String serviceName) {
        this.serviceName = serviceName;
    }
}
