package com.company.common.exception;

/**
 * exception class shows that script contains error
 */
public class ReadElementFromScriptException extends RuntimeException {
    public ReadElementFromScriptException(String message, Throwable cause) {
        super(message, cause);
    }
}
