package com.jug.demo.demo.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "demo_entity_spring")
public class DemoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    public DemoEntity(long l, String s) {
        this.id = l;
        this.name = s;
    }

    public DemoEntity() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}