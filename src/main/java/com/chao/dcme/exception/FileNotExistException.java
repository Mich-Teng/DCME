package com.chao.dcme.exception;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2014-11-02 20:32.
 * Package: com.chao.dcme.exception
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class FileNotExistException extends Exception {
    public FileNotExistException() {
    }

    public FileNotExistException(String message) {
        super(message);
    }

    public FileNotExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
