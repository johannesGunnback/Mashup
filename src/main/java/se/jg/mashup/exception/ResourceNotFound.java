package se.jg.mashup.exception;

import lombok.Getter;

@Getter
public class ResourceNotFound extends RuntimeException {

    private ErrorCode errorCode;

    public ResourceNotFound(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ResourceNotFound(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}
