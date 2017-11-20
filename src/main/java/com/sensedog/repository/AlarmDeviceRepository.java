package com.sensedog.repository;

import com.sensedog.repository.model.SqlAlarmDevice;
import com.sensedog.system.SessionProvider;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.time.ZonedDateTime;

@Service
public class AlarmDeviceRepository {

    private final SessionProvider provider;

    @Inject
    public AlarmDeviceRepository(final SessionProvider provider) {
        this.provider = provider;
    }

    public void update(final SqlAlarmDevice sqlAlarmDevice) {
        final Session session = provider.provide();
        session.getTransaction().begin();
        session.update(sqlAlarmDevice);
        session.getTransaction().commit();
        session.close();
    }

    public void updateLastSeen(final Integer id, final ZonedDateTime date) {
        final Session session = provider.provide();
        session.getTransaction().begin();
        final Query query = session.createQuery("UPDATE SqlAlarmDevice AS ad SET ad.lastSeen = :lastSeen WHERE ad.id = :id");
        query.setParameter("lastSeen", date);
        query.setParameter("id", id);
        query.executeUpdate();
        session.getTransaction().commit();
        session.close();
    }
}
