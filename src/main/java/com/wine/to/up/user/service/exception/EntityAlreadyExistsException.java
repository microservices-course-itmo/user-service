package com.wine.to.up.user.service.exception;

public class EntityAlreadyExistsException extends RuntimeException {
    /**
     * Exception with message "*entityName* with *field*: *value* is already exists"
     *
     * @param entityName name of the entity which cannot be found
     * @param field      key which is used to find entity
     * @param value      value of key
     */
    public EntityAlreadyExistsException(String entityName, String field, String value) {
        super(String.format("%s with %s: %s is already exists", entityName, field, value));
    }
}
