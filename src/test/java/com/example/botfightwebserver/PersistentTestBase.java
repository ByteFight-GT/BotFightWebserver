package com.example.botfightwebserver;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;

import jakarta.persistence.Entity;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
public class PersistentTestBase {

    @Autowired
    private EntityManager entityManager;

    public <T> void persistEntity(T entity) {
        entityManager.persist(entity);
        entityManager.flush();
    }

    public <T> T persistAndReturnEntity(T entity) {
        entityManager.persist(entity);
        entityManager.flush();
        return entity;
    }
}
