package com.sensedog.repository;

import com.sensedog.repository.model.SqlSubscriber;
import com.sensedog.system.SessionProvider;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriberRepository {

    private final SessionProvider provider;

    @Inject
    public SubscriberRepository(final SessionProvider provider) {
        this.provider = provider;
    }

    public Integer save(final SqlSubscriber subscriber) {
        final Session session = provider.provide();
        session.getTransaction().begin();
        final Serializable id = session.save(subscriber);
        session.getTransaction().commit();
        session.close();

        return (Integer) id;
    }

    public void updateLastNotifications(final List<SqlSubscriber> subscribers, final ZonedDateTime lastNotificationDate) {
        final Session session = provider.provide();
        session.getTransaction().begin();
        final Query query = session.createQuery("UPDATE Subscriber AS s SET s.lastNotificationDate = :lastNotificationDate WHERE s.id IN (:ids)");
        query.setParameter("lastNotificationDate", lastNotificationDate);
        query.setParameterList("ids", subscribers.stream().map(SqlSubscriber::getId).collect(Collectors.toList()));
        query.executeUpdate();
        session.getTransaction().commit();
        session.close();
    }
}
