DROP DATABASE sensedog_db;
CREATE DATABASE sensedog_db;
USE sensedog_db;

CREATE TABLE service (
    service_id              INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    master_user_id          INT,
    master_user_auth_token  VARCHAR(255),
    service_name            VARCHAR(255) NOT NULL,
    status                  VARCHAR(255) NOT NULL,
    creation_date           TIMESTAMP NOT NULL
);

CREATE TABLE alarm_device (
    service_id      INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    auth_token      VARCHAR(255) NOT NULL,
    cloud_token     VARCHAR(255) NOT NULL,
    battery         FLOAT,
    last_seen       TIMESTAMP,
    device_model    VARCHAR(255) NOT NULL,
    os_version      VARCHAR(255) NOT NULL,
    app_version     VARCHAR(255) NOT NULL,
    carrier         VARCHAR(255) NOT NULL
);

CREATE TABLE master_user (
    master_user_id  INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    cloud_token     VARCHAR(255) NOT NULL,
    phone           VARCHAR(255) NOT NULL,
    email           VARCHAR(255) NOT NULL,
    name            VARCHAR(255),
    UNIQUE (phone,email)
);

CREATE TABLE pin_code (
    service_id      INT NOT NULL PRIMARY KEY,
    pin_code        CHAR(6) NOT NULL,
    creation_date   TIMESTAMP NOT NULL,
    UNIQUE (pin_code)
);

CREATE TABLE detection (
    detection_id    INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    service_id      INT,
    detection_type  VARCHAR(255) NOT NULL,
    detection_value VARCHAR(255),
    detection_date  TIMESTAMP NOT NULL,
    severity        VARCHAR(255)
);

CREATE TABLE subscriber (
    subscriber_id              INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    service_id                 INT,
    name                       VARCHAR(255),
    phone                      VARCHAR(255) NOT NULL,
    email                      VARCHAR(255),
    notify_regularity          INT,
    last_notification_date     TIMESTAMP,
    last_notification_type     VARCHAR(255),
    last_notification_severity VARCHAR(255),
    minimum_severity           VARCHAR(255),
    security_key               VARCHAR(255) NOT NULL
);

CREATE TABLE subscriber_capability (
    subscriber_capability_id  INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    subscriber_id             INT,
    capability                VARCHAR(255) NOT NULL
);

ALTER TABLE alarm_device          ADD FOREIGN KEY (service_id)      REFERENCES service(service_id);
ALTER TABLE pin_code              ADD FOREIGN KEY (service_id)      REFERENCES service(service_id);
ALTER TABLE detection             ADD FOREIGN KEY (service_id)      REFERENCES service(service_id);
ALTER TABLE subscriber            ADD FOREIGN KEY (service_id)      REFERENCES service(service_id);
ALTER TABLE subscriber_capability ADD FOREIGN KEY (subscriber_id)   REFERENCES subscriber(subscriber_id);
ALTER TABLE service               ADD FOREIGN KEY (master_user_id)  REFERENCES master_user(master_user_id);
