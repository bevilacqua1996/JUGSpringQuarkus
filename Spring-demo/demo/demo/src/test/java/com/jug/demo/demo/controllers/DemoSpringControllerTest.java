package com.jug.demo.demo.controllers;

import com.jug.demo.demo.entities.DemoEntity;
import com.jug.demo.demo.repositories.DemoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DemoSpringControllerTest {

    @Mock
    private DemoRepository demoRepository;

    @InjectMocks
    private DemoSpringController demoSpringController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateEntity() {
        // Arrange
        DemoEntity demoEntity = new DemoEntity();
        demoEntity.setId(1L);
        demoEntity.setName("Test Entity");

        when(demoRepository.save(any(DemoEntity.class))).thenReturn(demoEntity);

        // Act
        ResponseEntity<DemoEntity> response = demoSpringController.createEntity(demoEntity);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("Test Entity", response.getBody().getName());
        verify(demoRepository, times(1)).save(demoEntity);
    }

    @Test
    void testGetAllEntities() {
        // Arrange
        List<DemoEntity> entityList = new ArrayList<>();
        entityList.add(new DemoEntity(1L, "Entity 1"));
        entityList.add(new DemoEntity(2L, "Entity 2"));

        when(demoRepository.findAll()).thenReturn(entityList);

        // Act
        ResponseEntity<List<DemoEntity>> response = demoSpringController.getAllEntities();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(demoRepository, times(1)).findAll();
    }

    @Test
    void testGetEntityById_WhenEntityExists() {
        // Arrange
        DemoEntity demoEntity = new DemoEntity(1L, "Test Entity");
        when(demoRepository.findById(1L)).thenReturn(Optional.of(demoEntity));

        // Act
        ResponseEntity<DemoEntity> response = demoSpringController.getEntityById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        verify(demoRepository, times(1)).findById(1L);
    }

    @Test
    void testGetEntityById_WhenEntityDoesNotExist() {
        // Arrange
        when(demoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<DemoEntity> response = demoSpringController.getEntityById(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(demoRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateEntity_WhenEntityExists() {
        // Arrange
        DemoEntity demoEntity = new DemoEntity(1L, "Updated Entity");
        when(demoRepository.existsById(1L)).thenReturn(true);
        when(demoRepository.save(any(DemoEntity.class))).thenReturn(demoEntity);

        // Act
        ResponseEntity<DemoEntity> response = demoSpringController.updateEntity(1L, demoEntity);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated Entity", response.getBody().getName());
        verify(demoRepository, times(1)).existsById(1L);
        verify(demoRepository, times(1)).save(demoEntity);
    }

    @Test
    void testUpdateEntity_WhenEntityDoesNotExist() {
        // Arrange
        DemoEntity demoEntity = new DemoEntity(1L, "Updated Entity");
        when(demoRepository.existsById(1L)).thenReturn(false);

        // Act
        ResponseEntity<DemoEntity> response = demoSpringController.updateEntity(1L, demoEntity);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(demoRepository, times(1)).existsById(1L);
        verify(demoRepository, times(0)).save(any(DemoEntity.class));
    }

    @Test
    void testDeleteEntity_WhenEntityExists() {
        // Arrange
        when(demoRepository.existsById(1L)).thenReturn(true);

        // Act
        ResponseEntity<Void> response = demoSpringController.deleteEntity(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(demoRepository, times(1)).existsById(1L);
        verify(demoRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteEntity_WhenEntityDoesNotExist() {
        // Arrange
        when(demoRepository.existsById(1L)).thenReturn(false);

        // Act
        ResponseEntity<Void> response = demoSpringController.deleteEntity(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(demoRepository, times(1)).existsById(1L);
        verify(demoRepository, times(0)).deleteById(anyLong());
    }
}