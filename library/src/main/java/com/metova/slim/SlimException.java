package com.metova.slim;

public class SlimException extends RuntimeException {

    public SlimException() {
        super();
    }

    public SlimException(String detailMessage) {
        super(detailMessage);
    }

    public SlimException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public SlimException(Throwable throwable) {
        super(throwable);
    }
}
