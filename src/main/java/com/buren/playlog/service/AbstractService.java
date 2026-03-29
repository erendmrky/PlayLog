package com.buren.playlog.service;

import com.buren.playlog.model.BaseEntity;
import com.buren.playlog.repository.AbstractRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public abstract class AbstractService<T extends BaseEntity, ID> {

    protected final AbstractRepository<T, ID> abstractRepository;

    protected AbstractService(AbstractRepository<T, ID> abstractRepository) {
        this.abstractRepository = abstractRepository;
    }

    protected T get(ID id) {
        return abstractRepository.findById(id)
                .filter(t -> t.isActive())
                .orElseThrow(() -> new EntityNotFoundException(getClass().getSimpleName() + " with id " + id + " not found or inactive"));
    }

    public void delete(ID id) {
        T entity = get(id);
        entity.setActive(false);
        abstractRepository.save(entity);
    }
}
