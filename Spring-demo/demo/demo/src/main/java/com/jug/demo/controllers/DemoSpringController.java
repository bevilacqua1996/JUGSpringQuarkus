package com.jug.demo.controllers;

import com.jug.demo.entities.DemoEntity;
import com.jug.demo.repositories.DemoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/demo") // Base URL para os endpoints
public class DemoSpringController {

    @Autowired
    private DemoRepository demoRepository;

    // Endpoint para criar uma nova entidade (Create)
    @PostMapping
    public ResponseEntity<DemoEntity> createEntity(@RequestBody DemoEntity demoEntity) {
        DemoEntity savedEntity = demoRepository.save(demoEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEntity);
    }

    // Endpoint para listar todas as entidades (Read - All)
    @GetMapping
    public ResponseEntity<List<DemoEntity>> getAllEntities() {
        List<DemoEntity> entities = demoRepository.findAll();
        return ResponseEntity.ok(entities);
    }

    // Endpoint para buscar uma entidade pelo ID (Read - Single)
    @GetMapping("/{id}")
    public ResponseEntity<DemoEntity> getEntityById(@PathVariable Long id) {
        Optional<DemoEntity> entity = demoRepository.findById(id);
        return entity.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Endpoint para atualizar uma entidade existente (Update)
    @PutMapping("/{id}")
    public ResponseEntity<DemoEntity> updateEntity(@PathVariable Long id, @RequestBody DemoEntity demoEntity) {
        if (!demoRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        demoEntity.setId(id); // Garante que o ID informado é mantido
        DemoEntity updatedEntity = demoRepository.save(demoEntity);
        return ResponseEntity.ok(updatedEntity);
    }

    // Endpoint para deletar uma entidade pelo ID (Delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntity(@PathVariable Long id) {
        if (!demoRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        demoRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}