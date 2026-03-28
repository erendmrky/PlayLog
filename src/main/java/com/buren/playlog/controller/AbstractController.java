package com.buren.playlog.controller;

import com.buren.playlog.model.BaseEntity;
import com.buren.playlog.service.AbstractService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public abstract class AbstractController<T extends BaseEntity, ID> {

    protected final AbstractService<T, ID> abstractService;

    protected AbstractController(AbstractService<T, ID> abstractService) {
        this.abstractService = abstractService;
    }
}
