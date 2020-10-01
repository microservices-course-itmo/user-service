package com.wine.to.up.user.service.exception;

public class EntityNotFoundException extends RuntimeException {
    /**
     * Exception with message "*field* with id: *id* is not found"
     * @param entityName name of the entity which cannot be found
     * @param id id of entity which cannot be found
     */
    public EntityNotFoundException(String entityName, String id) {
        super(String.format("%s with id: %s is not found", entityName, id));
    }

    /**
     * Exception with message "Entity with id: *id* is not found"
     * @param id id of entity which cannot be found
     */
    public EntityNotFoundException(String id) {
        this("Entity", id);
    }
}
