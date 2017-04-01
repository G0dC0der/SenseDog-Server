package com.sensedog.repository;

import com.sensedog.repository.entry.AlarmDevice;
import com.sensedog.system.SessionProvider;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.inject.Inject;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Date;

public class AlarmDeviceRepository {

    private final SessionProvider provider;

    @Inject
    public AlarmDeviceRepository(final SessionProvider provider) {
        this.provider = provider;
    }

    public void updateLastSeen(Integer id, ZonedDateTime date) {
        Session session = provider.provide();
        session.getTransaction().begin();
        Query query = session.createQuery("UPDATE AlarmDevice AS ad SET ad.lastSeen = :lastSeen WHERE ad.id = :id");
        query.setParameter("lastSeen", date);
        query.setParameter("id", id);
        query.executeUpdate();
        session.getTransaction().commit();
        session.close();
    }
}
