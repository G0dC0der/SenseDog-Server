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
@Table(name = "pin_code")
public class SqlPincode {

    @Id
    @Column(name = "service_id", unique = true, nullable = false)
    @GeneratedValue(generator = "generator")
    @GenericGenerator(name = "generator", strategy = "foreign", parameters = @Parameter(name = "property", value = "service"))
    private Integer id;

    @Column(name = "pin_code", unique = true, length=6, columnDefinition="CHAR")
    private String pinCode;

    @Column(name = "creation_date")
    private ZonedDateTime creationDate;

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

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(final String pinCode) {
        this.pinCode = pinCode;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(final ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public SqlService getService() {
        return service;
    }

    public void setService(final SqlService service) {
        this.service = service;
    }
}
