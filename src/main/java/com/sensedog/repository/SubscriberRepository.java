package com.sensedog.repository;

import com.sensedog.repository.entry.Subscriber;
import com.sensedog.system.SessionProvider;
import org.hibernate.Session;

import javax.inject.Inject;
import java.io.Serializable;

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
}
