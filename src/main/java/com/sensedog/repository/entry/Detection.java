package com.sensedog.repository.entry;

import com.sensedog.detection.DetectionType;
import com.sensedog.detection.Severity;

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
import java.time.ZonedDateTime;

@Entity
@Table(name = "detection")
public class Detection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detection_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private Service service;

    @Column(name = "detection_type")
    @Enumerated(EnumType.STRING)
    private DetectionType detectionType;

    @Column(name = "detection_value")
    private String value;

    @Column
    @Enumerated(EnumType.STRING)
    private Severity severity;

    @Column(name = "detection_date")
    private ZonedDateTime detectionDate;

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

    public DetectionType getDetectionType() {
        return detectionType;
    }

    public void setDetectionType(DetectionType detectionType) {
        this.detectionType = detectionType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public ZonedDateTime getDetectionDate() {
        return detectionDate;
    }

    public void setDetectionDate(ZonedDateTime detectionDate) {
        this.detectionDate = detectionDate;
    }
}
