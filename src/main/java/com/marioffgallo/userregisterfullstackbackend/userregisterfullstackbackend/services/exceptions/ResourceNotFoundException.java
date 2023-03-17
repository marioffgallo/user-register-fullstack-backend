package com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.services.exceptions;

/**
 * Class responsible for handling when a resource is not found exception
 *
 * @author Mario F.F Gallo
 * @version 1.0
 */
public class ResourceNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(Object id) {
        super("Resource not found. Id " + id);
    }
}
