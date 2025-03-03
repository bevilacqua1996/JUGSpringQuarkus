package com.jug.demo.repositories;

import com.jug.demo.entities.DemoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemoRepository extends JpaRepository<DemoEntity, Long> {
}