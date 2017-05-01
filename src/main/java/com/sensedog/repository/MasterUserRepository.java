package com.sensedog.repository;

import com.sensedog.repository.entry.MasterUser;
import com.sensedog.system.SessionProvider;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.inject.Inject;
import java.io.Serializable;

public class MasterUserRepository {

    private final SessionProvider provider;

    @Inject
    public MasterUserRepository(SessionProvider provider) {
        this.provider = provider;
    }

    public Integer save(MasterUser masterUser) {
        Session session = provider.provide();
        session.getTransaction().begin();
        Serializable id = session.save(masterUser);
        session.getTransaction().commit();
        session.close();

        return (Integer) id;
    }

    public MasterUser findByEmail(String email) {
        Session session = provider.provide();
        Query<MasterUser> query = session.createQuery("FROM MasterUser AS ms WHERE ms.email = :email", MasterUser.class);
        query.setParameter("email", email);
        MasterUser result = query.getResultList().isEmpty() ? null : query.getSingleResult();
        session.close();

        return result;
    }
}
