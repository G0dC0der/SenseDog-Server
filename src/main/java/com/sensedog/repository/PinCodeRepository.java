package com.sensedog.repository;

import com.sensedog.repository.model.SqlPincode;
import com.sensedog.system.SessionProvider;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;

@Service
public class PinCodeRepository {

    private final SessionProvider provider;

    @Inject
    public PinCodeRepository(final SessionProvider provider) {
        this.provider = provider;
    }

    public SqlPincode getByPinCode(final String pinCode) {
        final Session session = provider.provide();
        final Query<SqlPincode> query = session.createQuery("FROM PinCode AS p WHERE p.pinCode = :pinCode", SqlPincode.class);
        query.setParameter("pinCode", pinCode);
        final SqlPincode result = query.getResultList().isEmpty() ? null : query.getSingleResult();
        session.close();

        return result;
    }
}
