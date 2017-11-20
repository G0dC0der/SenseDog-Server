package com.sensedog.repository;

import com.sensedog.repository.model.SqlService;
import com.sensedog.system.SessionProvider;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

@org.jvnet.hk2.annotations.Service
public class ServiceRepository {

    private final SessionProvider provider;

    @Inject
    public ServiceRepository(final SessionProvider provider) {
        this.provider = provider;
    }

    public Integer create(final SqlService service) {
        final Session session = provider.provide();
        session.getTransaction().begin();
        final Serializable id = session.save(service);
        session.getTransaction().commit();
        session.close();

        return (Integer) id;
    }

    public void update(final SqlService service) {
        final Session session = provider.provide();
        session.getTransaction().begin();
        session.update(service);
        session.getTransaction().commit();
        session.close();
    }

    public SqlService findByAlarmDeviceToken(final String token) {
        final Session session = provider.provide();
        final Query query = session.createQuery("FROM Service AS s WHERE s.alarmDevice.authToken = :token");
        query.setParameter("token", token);
        final Object result = query.getResultList().isEmpty() ? null : query.getSingleResult();
        session.close();

        return (SqlService) result;
    }

    public SqlService findByMasterToken(final String token) {
        final Session session = provider.provide();
        final Query query = session.createQuery("FROM Service AS s WHERE s.masterAuthToken = :token");
        query.setParameter("token", token);
        final Object result = query.getResultList().isEmpty() ? null : query.getSingleResult();
        session.close();

        return (SqlService) result;
    }

    public SqlService[] findServicesByMasterEmail(final String email) {
        final Session session = provider.provide();
        final Query<SqlService> query = session.createQuery("FROM Service AS s WHERE s.masterUser.email = :email", SqlService.class);
        query.setParameter("email", email);
        final List<SqlService> result = query.getResultList();
        session.close();

        return result.toArray(new SqlService[result.size()]);
    }
}