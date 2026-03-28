package com.buren.playlog.controller;

import com.buren.playlog.service.AbstractService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public abstract class AbstractController<T, ID> {

    private final AbstractService<T, ID> abstractService;

    public AbstractController(AbstractService<T, ID> abstractService) {
        this.abstractService = abstractService;
    }
}
