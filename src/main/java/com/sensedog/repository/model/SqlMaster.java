package com.sensedog.repository.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "master_user")
public class SqlMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "master_user_id")
    private Integer id;

    @Column(name = "cloud_token")
    private String cloudToken;

    @Column
    private String name;

    @Column(unique = true)
    private String phone;

    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "masterUser", fetch = FetchType.LAZY)
    private List<SqlService> services;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getCloudToken() {
        return cloudToken;
    }

    public void setCloudToken(final String cloudToken) {
        this.cloudToken = cloudToken;
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

    public List<SqlService> getServices() {
        return services;
    }

    public void setServices(final List<SqlService> services) {
        this.services = services;
    }
}
