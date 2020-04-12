package se.jg.mashup.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    ARTIST_NOT_FOUND("Could not find artist");

    private String msg;

    ErrorCode(String msg) {
        this.msg = msg;
    }
}
