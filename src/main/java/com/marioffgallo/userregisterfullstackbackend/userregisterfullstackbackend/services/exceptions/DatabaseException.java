package com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.services.exceptions;

/**
 * Class responsible for handling database exceptions
 *
 * @author Mario F.F Gallo
 * @version 1.0
 */
public class DatabaseException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public DatabaseException(String msg) {
        super(msg);
    }
}
