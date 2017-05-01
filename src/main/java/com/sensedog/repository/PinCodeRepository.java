package com.sensedog.repository;

import com.sensedog.repository.entry.PinCode;
import com.sensedog.system.SessionProvider;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.inject.Inject;

public class PinCodeRepository {

    private final SessionProvider provider;

    @Inject
    public PinCodeRepository(SessionProvider provider) {
        this.provider = provider;
    }

    public PinCode getByPinCode(String pinCode) {
        Session session = provider.provide();
        Query<PinCode> query = session.createQuery("FROM PinCode AS p WHERE p.pinCode = :pinCode", PinCode.class);
        query.setParameter("pinCode", pinCode);
        PinCode result = query.getResultList().isEmpty() ? null : query.getSingleResult();
        session.close();

        return result;
    }
}
