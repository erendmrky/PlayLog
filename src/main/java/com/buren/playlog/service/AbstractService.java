package com.buren.playlog.service;

import com.buren.playlog.repository.AbstractRepository;
import org.springframework.stereotype.Service;

@Service
public abstract class AbstractService<T, ID> {

    private final AbstractRepository<T, ID> abstractRepository;

    public AbstractService(AbstractRepository<T, ID> abstractRepository) {
        this.abstractRepository = abstractRepository;
    }
}
