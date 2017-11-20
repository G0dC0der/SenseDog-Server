package com.sensedog.repository;

import com.sensedog.repository.model.SqlDetection;
import com.sensedog.system.SessionProvider;
import org.hibernate.Session;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class DetectionRepository {

    private final SessionProvider provider;

    @Inject
    public DetectionRepository(final SessionProvider provider) {
        this.provider = provider;
    }

    public Integer save(final SqlDetection detection) {
        final Session session = provider.provide();
        session.getTransaction().begin();
        final Serializable id = session.save(detection);
        session.getTransaction().commit();
        session.close();

        return (Integer) id;
    }

    public List<SqlDetection> getBetween(final Integer serviceId, final ZonedDateTime start, final ZonedDateTime end) {
        final Session session = provider.provide();
        final TypedQuery<SqlDetection> query = session.createQuery("FROM Detection AS t WHERE t.service.id = :serviceId AND t.detectionDate BETWEEN :resume AND :end", SqlDetection.class);
        query.setParameter("serviceId", serviceId);
        query.setParameter("resume", start);
        query.setParameter("end", end);
        final List<SqlDetection> result = query.getResultList();
        session.close();

        return result;
    }
}
