package com.noname.forum.db;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

@Testcontainers 
public abstract class AbstractPostgresIT {
    
    @ServiceConnection 
    static final PostgreSQLContainer POSTGRES =
        new PostgreSQLContainer("postgres:16-alpine");

    static {
        POSTGRES.start();
    }
}
