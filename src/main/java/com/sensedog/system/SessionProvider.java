package com.sensedog.system;

import org.hibernate.Session;

public interface SessionProvider {

    Session provide();
}