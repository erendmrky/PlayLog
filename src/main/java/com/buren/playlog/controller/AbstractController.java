package com.buren.playlog.controller;

import com.buren.playlog.model.BaseEntity;
import com.buren.playlog.service.AbstractService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public abstract class AbstractController<T extends BaseEntity, ID> {

    protected final AbstractService<T, ID> abstractService;

    protected AbstractController(AbstractService<T, ID> abstractService) {
        this.abstractService = abstractService;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable ID id) {
        try {
            abstractService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException _) {
            return ResponseEntity.notFound().build();
        }
    }
}
