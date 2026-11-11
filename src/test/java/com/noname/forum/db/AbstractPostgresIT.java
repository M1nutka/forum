package com.noname.forum.db;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.postgresql.PostgreSQLContainer;

public abstract class AbstractPostgresIT {
    
    @ServiceConnection 
    static final PostgreSQLContainer POSTGRES =
        new PostgreSQLContainer("postgres:16-alpine");

    static {
        POSTGRES.start();
    }
}
