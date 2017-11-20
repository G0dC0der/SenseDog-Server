package com.sensedog.repository;

import com.sensedog.repository.model.SqlMaster;
import com.sensedog.system.SessionProvider;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.io.Serializable;

@Service
public class MasterUserRepository {

    private final SessionProvider provider;

    @Inject
    public MasterUserRepository(final SessionProvider provider) {
        this.provider = provider;
    }

    public Integer save(final SqlMaster masterUser) {
        final Session session = provider.provide();
        session.getTransaction().begin();
        final Serializable id = session.save(masterUser);
        session.getTransaction().commit();
        session.close();

        return (Integer) id;
    }

    public SqlMaster findByEmail(final String email) {
        final Session session = provider.provide();
        final Query<SqlMaster> query = session.createQuery("FROM MasterUser AS ms WHERE ms.email = :email", SqlMaster.class);
        query.setParameter("email", email);
        final SqlMaster result = query.getResultList().isEmpty() ? null : query.getSingleResult();
        session.close();

        return result;
    }
}
