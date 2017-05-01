package com.sensedog.repository;

import com.sensedog.repository.entry.Service;
import com.sensedog.system.SessionProvider;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ServiceRepository {

    private final SessionProvider provider;

    @Inject
    public ServiceRepository(final SessionProvider provider) {
        this.provider = provider;
    }

    public Integer create(Service service) {
        Session session = provider.provide();
        session.getTransaction().begin();
        Serializable id = session.save(service);
        session.getTransaction().commit();
        session.close();

        return (Integer) id;
    }

    public void update(Service service) {
        Session session = provider.provide();
        session.getTransaction().begin();
        session.update(service);
        session.getTransaction().commit();
        session.close();
    }

    public Service findByAlarmDeviceToken(String token) {
        Session session = provider.provide();
        Query query = session.createQuery("FROM Service AS s WHERE s.alarmDevice.authToken = :token");
        query.setParameter("token", token);
        Object result = query.getResultList().isEmpty() ? null : query.getSingleResult();
        session.close();

        return (Service) result;
    }

    public Service findByMasterToken(String token) {
        Session session = provider.provide();
        Query query = session.createQuery("FROM Service AS s WHERE s.masterAuthToken = :token");
        query.setParameter("token", token);
        Object result = query.getResultList().isEmpty() ? null : query.getSingleResult();
        session.close();

        return (Service) result;
    }

    public Service[] findServicesByMasterEmail(String email) {
        Session session = provider.provide();
        Query<Service> query = session.createQuery("FROM Service AS s WHERE s.masterUser.email = :email", Service.class);
        query.setParameter("email", email);
        List<Service> result = query.getResultList();
        session.close();

        return result.toArray(new Service[result.size()]);
    }
}