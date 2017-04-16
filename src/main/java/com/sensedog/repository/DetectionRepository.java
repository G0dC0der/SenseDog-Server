package com.sensedog.repository;

import com.sensedog.repository.entry.Detection;
import com.sensedog.system.SessionProvider;
import org.hibernate.Session;

import javax.inject.Inject;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

public class DetectionRepository {

    private final SessionProvider provider;

    @Inject
    public DetectionRepository(final SessionProvider provider) {
        this.provider = provider;
    }

    public Integer save(Detection detection) {
        Session session = provider.provide();
        session.getTransaction().begin();
        Serializable id = session.save(detection);
        session.getTransaction().commit();
        session.close();

        return (Integer) id;
    }

    public List<Detection> getBetween(Integer serviceId, ZonedDateTime start, ZonedDateTime end) {
        Session session = provider.provide();
        TypedQuery<Detection> query = session.createQuery("FROM Detection AS t WHERE t.service.serviceId = :serviceId AND t.detectionDate BETWEEN :resume AND :end", Detection.class);
        query.setParameter("serviceId", serviceId);
        query.setParameter("resume", start);
        query.setParameter("end", end);
        List<Detection> result = query.getResultList();
        session.close();

        return result;
    }
}
