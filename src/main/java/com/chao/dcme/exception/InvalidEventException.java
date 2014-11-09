package com.chao.dcme.exception;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2014-11-06 09:32.
 * Package: com.chao.dcme.exception
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class InvalidEventException extends Exception {

    public InvalidEventException() {
    }

    public InvalidEventException(String message) {
        super(message);
    }

    public InvalidEventException(String message, Throwable cause) {
        super(message, cause);
    }
}
