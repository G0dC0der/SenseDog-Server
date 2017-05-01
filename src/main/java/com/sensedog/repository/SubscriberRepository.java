package com.sensedog.repository;

import com.sensedog.repository.entry.Subscriber;
import com.sensedog.system.SessionProvider;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.inject.Inject;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class SubscriberRepository {

    private final SessionProvider provider;

    @Inject
    public SubscriberRepository(final SessionProvider provider) {
        this.provider = provider;
    }

    public Integer save(Subscriber subscriber) {
        Session session = provider.provide();
        session.getTransaction().begin();
        Serializable id = session.save(subscriber);
        session.getTransaction().commit();
        session.close();

        return (Integer) id;
    }

    public void updateLastNotifications(List<Subscriber> subscribers, ZonedDateTime lastNotificationDate) {
        Session session = provider.provide();
        session.getTransaction().begin();
        Query query = session.createQuery("UPDATE Subscriber AS s SET s.lastNotificationDate = :lastNotificationDate WHERE s.id IN (:ids)");
        query.setParameter("lastNotificationDate", lastNotificationDate);
        query.setParameterList("ids", subscribers.stream().map(Subscriber::getId).collect(Collectors.toList()));
        query.executeUpdate();
        session.getTransaction().commit();
        session.close();
    }
}
