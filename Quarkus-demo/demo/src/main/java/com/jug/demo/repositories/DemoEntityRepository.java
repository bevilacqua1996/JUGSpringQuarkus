package com.jug.demo.repositories;

import com.jug.demo.entities.DemoEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DemoEntityRepository implements PanacheRepository<DemoEntity> {
    // Custom query methods can be added here
}