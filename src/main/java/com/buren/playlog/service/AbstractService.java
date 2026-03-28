package com.buren.playlog.service;

import com.buren.playlog.model.BaseEntity;
import com.buren.playlog.repository.AbstractRepository;
import org.springframework.stereotype.Service;

@Service
public abstract class AbstractService<T extends BaseEntity, ID> {

    protected final AbstractRepository<T, ID> abstractRepository;

    protected AbstractService(AbstractRepository<T, ID> abstractRepository) {
        this.abstractRepository = abstractRepository;
    }
}
