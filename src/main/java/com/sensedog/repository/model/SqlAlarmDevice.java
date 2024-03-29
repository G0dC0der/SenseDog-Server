package com.sensedog.repository.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Entity
@Table(name = "alarm_device")
public class SqlAlarmDevice {

    @Id
    @GeneratedValue(generator = "generator")
    @GenericGenerator(name = "generator", strategy = "foreign", parameters = @Parameter(name = "property", value = "service"))
    @Column(name = "service_id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "auth_token")
    private String authToken;

    @Column(name = "cloud_token")
    private String cloudToken;

    @Column
    private Float battery;

    @Column(name = "last_seen")
    private ZonedDateTime lastSeen;

    @Column(name = "device_model")
    private String deviceModel;

    @Column(name = "os_version")
    private String osVersion;

    @Column(name = "app_version")
    private String appVersion;

    @Column
    private String carrier;

    @OneToOne
    @PrimaryKeyJoinColumn
    @JoinColumn(name = "service_id")
    private SqlService service;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(final String authToken) {
        this.authToken = authToken;
    }

    public String getCloudToken() {
        return cloudToken;
    }

    public void setCloudToken(final String cloudToken) {
        this.cloudToken = cloudToken;
    }

    public Float getBattery() {
        return battery;
    }

    public void setBattery(final Float battery) {
        this.battery = battery;
    }

    public ZonedDateTime getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(final ZonedDateTime lastSeen) {
        this.lastSeen = lastSeen;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(final String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(final String osVersion) {
        this.osVersion = osVersion;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(final String appVersion) {
        this.appVersion = appVersion;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(final String carrier) {
        this.carrier = carrier;
    }

    public SqlService getService() {
        return service;
    }

    public void setService(final SqlService service) {
        this.service = service;
    }
}
